# InA Compressor GUI Capture Field Map

Capture date: 2026-05-07

All captures were filtered to command `0x30`, channel byte `0x00` (`InA`).
Observed payload length was 19 bytes:

```text
00 01 10 30 00 RR 00 AA AA LL LL KK 00 TT TT FF FF XX YY
```

Observed field map:

| Control | Changed payload offset(s) | Encoding observed | Captured values |
| --- | ---: | --- | --- |
| Ratio | `0x05` | u8 enum, byte `0x06` stayed `00` | `1..15` |
| Attack | `0x07..0x08` | u16le | `310, 619, 964, 998` |
| Release | `0x09..0x0A` | u16le | `731, 1143, 1659, 2380, 2999` |
| Knee | `0x0B` | u8 enum/value, byte `0x0C` stayed `00` | `1..12` |
| Threshold | `0x0D..0x0E` | u16le; capture only changed low byte because values stayed below `0x0100` | `212, 182, 91, 0` |
| Freq | `0x0F..0x10` | u16le | `176, 300` |
| Type | `0x12` | u8 enum/value | `1, 3, 4, 6, 9` |

Offset `0x11` stayed constant during this InA capture (`0x23`) and is not the Type value.
Earlier InC Type/Freq captures also show Type changing at offset `0x12` while offset `0x11` stays constant.

Per-control output folders:

- `out/fader/ina-comp-threshold-series`
- `out/fader/ina-comp-attack-series`
- `out/fader/ina-comp-freq-series`
- `out/fader/ina-comp-ratio-series`
- `out/fader/ina-comp-release-series`
- `out/fader/ina-comp-knee-series`
- `out/fader/ina-comp-type-series`

Scripts used:

- `script-example/251-auto-series-ina-comp-threshold.dspd`
- `script-example/252-auto-series-ina-comp-attack.dspd`
- `script-example/253-auto-series-ina-comp-freq.dspd`
- `script-example/254-auto-series-ina-comp-ratio.dspd`
- `script-example/255-auto-series-ina-comp-release.dspd`
- `script-example/256-auto-series-ina-comp-knee.dspd`
- `script-example/257-auto-series-ina-comp-type.dspd`
