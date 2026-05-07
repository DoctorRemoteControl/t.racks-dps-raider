# Out1 IIR Crossover GUI Capture Field Map

Scripts:

- `script-example/360-auto-series-out1-iir-highpass-frequency.dspd`
- `script-example/361-auto-series-out1-iir-highpass-slope.dspd`
- `script-example/362-auto-capture-out1-iir-highpass-bypass.dspd`
- `script-example/363-auto-series-out1-iir-lowpass-frequency.dspd`
- `script-example/364-auto-series-out1-iir-lowpass-slope.dspd`
- `script-example/365-auto-capture-out1-iir-lowpass-bypass.dspd`
- `script-example/366-auto-series-out1-iir-highpass-slope-long.dspd`
- `script-example/367-auto-series-out1-iir-lowpass-slope-long.dspd`

## Write Payloads

Fresh FIR408 Out1 IIR crossover GUI captures use 9-byte payloads:

HighPass:

`00 01 06 32 04 <frequency_lo> <frequency_hi> <state_or_slope> 0A`

LowPass:

`00 01 06 31 04 <frequency_lo> <frequency_hi> <state_or_slope> 0A`

| Field | Payload Offset | Meaning |
| --- | --- | --- |
| command | `3` | `0x32` HighPass, `0x31` LowPass |
| output channel | `4` | `0x04` for Out1 |
| frequency | `5..6` | u16le, raw 0..300 |
| state/slope | `7` | `0x00` bypass, `0x01..0x14` slope enum |
| tail/context | `8` | observed constant `0x0A` in Out1 IIR captures |

## Capture Results

| Capture | Series Count | Changing Offsets | Last Payload |
| --- | ---: | --- | --- |
| HighPass Frequency | 42 | `[5, 6]` | `00 01 06 32 04 00 00 00 0A` |
| HighPass Slope short | 1 | `[]` | `00 01 06 32 04 00 00 00 0A` |
| HighPass Bypass | 1 | `[]` | `00 01 06 32 04 00 00 01 0A` |
| LowPass Frequency | 11 | `[5, 6]` | `00 01 06 31 04 00 00 00 0A` |
| LowPass Slope short | 1 | `[]` | `00 01 06 31 04 00 00 00 0A` |
| LowPass Bypass | 1 | `[]` | `00 01 06 31 04 00 00 01 0A` |
| HighPass Slope long | 20 | `[7]` | `00 01 06 32 04 00 00 14 0A` |
| LowPass Slope long | 19 | `[7]` | `00 01 06 31 04 00 00 14 0A` |

The long slope captures confirmed state/slope byte `0x01..0x14` in enum order. The fixed trailing byte stayed `0x0A` and is not the slope.

This supersedes older inherited/predicted FIR408 crossover notes that used the shorter `00 01 05 31/32 ...` payload.
