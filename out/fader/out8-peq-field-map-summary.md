# Out8 PEQ GUI Capture Field Map

Scripts:

- `script-example/349-auto-series-out8-peq1-frequency.dspd`
- `script-example/350-auto-series-out8-peq1-q.dspd`
- `script-example/351-auto-series-out8-peq1-gain.dspd`
- `script-example/352-auto-capture-out8-peq1-type.dspd`
- `script-example/353-auto-capture-out8-peq1-bypass.dspd`
- `script-example/354-auto-series-out8-peq9-frequency.dspd`
- `script-example/355-auto-series-out8-peq9-q.dspd`
- `script-example/356-auto-series-out8-peq9-gain.dspd`
- `script-example/357-auto-capture-out8-peq9-type.dspd`
- `script-example/358-auto-capture-out8-peq9-bypass.dspd`

## Write Payload

Out8 output PEQ uses command `0x33` with the same compact payload layout as Out1:

`00 01 0A 33 0B <band> <gain_lo> <gain_hi> <frequency_lo> <frequency_hi> <q_raw> <type> <bypass>`

| Field | Payload Offset | Observed |
| --- | --- | --- |
| Output channel | `4` | `0x0B` for Out8 |
| PEQ band | `5` | `0x00` for PEQ1, `0x08` for PEQ9 |
| Gain | `6..7` | u16le, moved in gain captures |
| Frequency | `8..9` | u16le, moved in frequency captures |
| Q | `10` | u8, moved in Q captures |
| Type | `11` | u8, observed in endpoint captures |
| Bypass | `12` | u8, observed in endpoint captures |

## Capture Results

| Capture | Series Count | Changing Offsets | Last Payload |
| --- | ---: | --- | --- |
| Out8 PEQ1 Frequency | 16 | `[8, 9]` | `00 01 0A 33 0B 00 78 00 2C 01 23 00 00` |
| Out8 PEQ1 Q | 19 | `[10]` | `00 01 0A 33 0B 00 78 00 2C 01 64 00 00` |
| Out8 PEQ1 Gain | 24 | `[6]` | `00 01 0A 33 0B 00 F0 00 2C 01 64 00 00` |
| Out8 PEQ1 Type | 2 | `[]` | `00 01 0A 33 0B 00 F0 00 2C 01 0A 01 00` |
| Out8 PEQ1 Bypass | 1 | `[]` | `00 01 0A 33 0B 00 F0 00 2C 01 0A 01 01` |
| Out8 PEQ9 Frequency | 23 | `[8, 9]` | `00 01 0A 33 0B 08 78 00 00 00 23 00 00` |
| Out8 PEQ9 Q | 20 | `[10]` | `00 01 0A 33 0B 08 78 00 00 00 00 00 00` |
| Out8 PEQ9 Gain | 21 | `[6]` | `00 01 0A 33 0B 08 00 00 00 00 00 00 00` |
| Out8 PEQ9 Type | 2 | `[]` | `00 01 0A 33 0B 08 00 00 00 00 23 08 00` |
| Out8 PEQ9 Bypass | 1 | `[]` | `00 01 0A 33 0B 08 00 00 00 00 23 08 01` |

The one-shot Type and Bypass captures can have empty `changingOffsets` because the capture only contains identical endpoint payloads or a single payload. Their last payloads still confirm the field offsets together with the Out1 captures and the Out8 channel/band bytes.
