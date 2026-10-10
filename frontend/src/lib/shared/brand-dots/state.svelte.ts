import { PRESETS, presetSize, type PresetName } from './presets';

export const brandDots = $state({
	preset: 'hidden' as PresetName,
	/** top-left of the dot group, in document coordinates */
	x: 0,
	y: 0
});

/** Center the preset on the given rect (converted to document coordinates). */
export function placeAt(rect: DOMRect, preset: PresetName) {
	const { width, height } = presetSize(PRESETS[preset]);
	brandDots.preset = preset;
	brandDots.x = rect.left + window.scrollX + rect.width / 2 - width / 2;
	brandDots.y = rect.top + window.scrollY + rect.height / 2 - height / 2;
}

export function hideDots() {
	brandDots.preset = 'hidden';
}
