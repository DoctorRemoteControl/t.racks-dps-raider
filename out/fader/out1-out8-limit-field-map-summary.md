# Out1 / Out8 Limiter GUI Capture Field Map

Capture date: 2026-05-07

All captures were filtered to command `0x3F`.

Observed FIR408 payload length was 15 bytes:

```text
00 01 0C 3F CC RR 00 AA AA LL LL KK 00 TT TT
```

Where `CC` is the output channel byte:

- Out1 = `0x04`
- Out8 = `0x0B`

Observed field map:

| Control | Changed payload offset(s) | Encoding observed | Out1 values | Out8 values |
| --- | ---: | --- | --- | --- |
| Ratio | `0x05` | u8 enum, byte `0x06` stayed `00` | `14..0` | `14..0` |
| Attack | `0x07..0x08` | u16le | `69, 275, 998` | `69, 344, 895, 998` |
| Release | `0x09..0x0A` | u16le | `628, 1968, 2999` | `525, 1762, 2999` |
| Knee | `0x0B` | u8 enum/value, byte `0x0C` stayed `00` | `1..12` | `1..12` |
| Threshold | `0x0D..0x0E` | u16le | `212, 144, 0` | `212, 190, 144, 129, 114, 61, 30, 8, 0` |

Per-control output folders:

- `out/fader/out1-limit-threshold-series`
- `out/fader/out1-limit-attack-series`
- `out/fader/out1-limit-ratio-series`
- `out/fader/out1-limit-release-series`
- `out/fader/out1-limit-knee-series`
- `out/fader/out8-limit-threshold-series-v2`
- `out/fader/out8-limit-attack-series-v2`
- `out/fader/out8-limit-ratio-series-v2`
- `out/fader/out8-limit-release-series-v2`
- `out/fader/out8-limit-knee-series-v2`

Scripts used:

- `script-example/266-auto-series-out1-limit-threshold.dspd`
- `script-example/267-auto-series-out1-limit-attack.dspd`
- `script-example/268-auto-series-out1-limit-ratio.dspd`
- `script-example/269-auto-series-out1-limit-release.dspd`
- `script-example/270-auto-series-out1-limit-knee.dspd`
- `script-example/271-auto-series-out8-limit-threshold.dspd`
- `script-example/272-auto-series-out8-limit-attack.dspd`
- `script-example/273-auto-series-out8-limit-ratio.dspd`
- `script-example/274-auto-series-out8-limit-release.dspd`
- `script-example/275-auto-series-out8-limit-knee.dspd`
