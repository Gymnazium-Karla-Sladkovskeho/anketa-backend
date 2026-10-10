import { hideDots, placeAt } from './state.svelte';
import type { PresetName } from './presets';

type Params = { preset: PresetName };

let active: HTMLElement | null = null;

/**
 * Svelte action: marks an element as the place where the dots should appear.
 *
 *   <div use:dotsAnchor={{ preset: 'logo' }}></div>
 *
 * The dots move to the center of this element in the shape of `preset`
 * (see presets.ts) and follow it when the layout changes.
 * Changing `preset` on the same element morphs the dots in place.
 *
 * Tips:
 * - Use an element with a fixed size in the normal page flow, so the layout
 *   does not shift when the preset changes.
 * - If the next page has no anchor, the dots shrink to nothing.
 * - Similar to matchedGeometryEffect in SwiftUI.
 */
export function dotsAnchor(node: HTMLElement, params: Params) {
	let preset = params.preset;
	const sync = () => placeAt(node.getBoundingClientRect(), preset);

	active = node;
	sync();

	const observer = new ResizeObserver(sync);
	observer.observe(node);
	observer.observe(document.documentElement);
	window.addEventListener('resize', sync);

	return {
		update(next: Params) {
			preset = next.preset;
			sync();
		},
		destroy() {
			observer.disconnect();
			window.removeEventListener('resize', sync);
			// If the next page mounts its own anchor in the same tick, that one overrides this.
			if (active === node) {
				active = null;
				hideDots();
			}
		}
	};
}
