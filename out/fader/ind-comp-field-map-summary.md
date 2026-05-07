# InD Compressor GUI Capture Field Map

Capture date: 2026-05-07

All captures were filtered to command `0x30`, channel byte `0x03` (`InD`).
Observed payload length was 19 bytes:

```text
00 01 10 30 03 RR 00 AA AA LL LL KK 00 TT TT FF FF XX YY
```

Observed field map:

| Control | Changed payload offset(s) | Encoding observed | Captured values |
| --- | ---: | --- | --- |
| Ratio | `0x05` | u8 enum, byte `0x06` stayed `00` | `14..0` |
| Attack | `0x07..0x08` | u16le | `69, 413, 964, 998` |
| Release | `0x09..0x0A` | u16le | `628, 2793, 2999` |
| Knee | `0x0B` | u8 enum/value, byte `0x0C` stayed `00` | `1..12` |
| Threshold | `0x0D..0x0E` | u16le; capture only changed low byte because values stayed below `0x0100` | `212, 23, 0` |
| Freq | `0x0F..0x10` | u16le | `228, 300` |
| Type | `0x12` | u8 enum/value | `1, 8, 9` |

Offset `0x11` stayed constant during this InD capture (`0x23`) and is not the Type value.

Per-control output folders:

- `out/fader/ind-comp-threshold-series`
- `out/fader/ind-comp-attack-series`
- `out/fader/ind-comp-freq-series`
- `out/fader/ind-comp-ratio-series`
- `out/fader/ind-comp-release-series`
- `out/fader/ind-comp-knee-series`
- `out/fader/ind-comp-type-series`

Scripts used:

- `script-example/258-auto-series-ind-comp-threshold.dspd`
- `script-example/259-auto-series-ind-comp-attack.dspd`
- `script-example/260-auto-series-ind-comp-freq.dspd`
- `script-example/261-auto-series-ind-comp-ratio.dspd`
- `script-example/262-auto-series-ind-comp-release.dspd`
- `script-example/263-auto-series-ind-comp-knee.dspd`
- `script-example/264-auto-series-ind-comp-type.dspd`
