# Out8 IIR Crossover GUI Capture Field Map

Scripts:

- `script-example/369-auto-series-out8-iir-highpass-frequency.dspd`
- `script-example/370-auto-series-out8-iir-highpass-slope-long.dspd`
- `script-example/371-auto-capture-out8-iir-highpass-bypass.dspd`
- `script-example/372-auto-series-out8-iir-lowpass-frequency.dspd`
- `script-example/373-auto-series-out8-iir-lowpass-slope-long.dspd`
- `script-example/374-auto-capture-out8-iir-lowpass-bypass.dspd`

## Write Payloads

Fresh FIR408 Out8 IIR crossover GUI captures use 9-byte payloads:

HighPass:

`00 01 06 32 0B <frequency_lo> <frequency_hi> <state_or_slope> 0A`

LowPass:

`00 01 06 31 0B <frequency_lo> <frequency_hi> <state_or_slope> 0A`

| Field | Payload Offset | Meaning |
| --- | --- | --- |
| command | `3` | `0x32` HighPass, `0x31` LowPass |
| output channel | `4` | `0x0B` for Out8 |
| frequency | `5..6` | u16le, raw 0..300 |
| state/slope | `7` | `0x00` bypass, `0x01..0x14` slope enum |
| tail/context | `8` | observed constant `0x0A` in Out8 IIR captures |

## Capture Results

| Capture | Series Count | Changing Offsets | Last Payload |
| --- | ---: | --- | --- |
| HighPass Frequency | 111 | `[5, 6]` | `00 01 06 32 0B 2C 01 0C 0A` |
| HighPass Slope long | 20 | `[7]` | `00 01 06 32 0B 2C 01 01 0A` |
| HighPass Bypass | 1 | `[]` | `00 01 06 32 0B 2C 01 00 0A` |
| LowPass Frequency | 106 | `[5, 6]` | `00 01 06 31 0B 00 00 14 0A` |
| LowPass Slope long | 19 | `[7]` | `00 01 06 31 0B 00 00 01 0A` |
| LowPass Bypass | 1 | `[]` | `00 01 06 31 0B 00 00 00 0A` |

The HighPass slope capture observed the full `0x14..0x01` selector run at payload offset `7`. The LowPass slope capture observed `0x13..0x01`; the preceding LowPass frequency capture held `0x14`, so the same `0x01..0x14` slope enum model is confirmed for Out8 LowPass too.

This confirms the fresh 9-byte FIR408 Out8 IIR layout and supersedes older inherited/predicted crossover notes that used the shorter `00 01 05 31/32 ...` payload.
