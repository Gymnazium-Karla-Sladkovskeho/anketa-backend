export type Dot = { x: number; y: number; size: number };
export type DotPreset = readonly [Dot, Dot, Dot, Dot];

/** Add new shapes here. Every preset must have exactly 4 dots (the 4 school colors). */
export const PRESETS = {
	hidden: [
		{ x: 0, y: 0, size: 0 },
		{ x: 0, y: 0, size: 0 },
		{ x: 0, y: 0, size: 0 },
		{ x: 0, y: 0, size: 0 }
	],
	logo: [
		{ x: 30, y: 0, size: 60 },
		{ x: 0, y: 30, size: 60 },
		{ x: 60, y: 30, size: 60 },
		{ x: 30, y: 60, size: 60 }
	],
	loading: [
		{ x: 0, y: 0, size: 40 },
		{ x: 60, y: 0, size: 40 },
		{ x: 120, y: 0, size: 40 },
		{ x: 180, y: 0, size: 40 }
	]
} as const satisfies Record<string, DotPreset>;

export type PresetName = keyof typeof PRESETS;

export function presetSize(preset: DotPreset) {
	return {
		width: Math.max(...preset.map((d) => d.x + d.size)),
		height: Math.max(...preset.map((d) => d.y + d.size))
	};
}
