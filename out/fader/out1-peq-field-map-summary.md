# Out1 PEQ GUI Capture Field Map

Fresh GUI captures:

- `out/fader/out1-peq1-frequency-series`
- `out/fader/out1-peq1-q-series`
- `out/fader/out1-peq1-gain-series`
- `out/fader/out1-peq1-type-series`
- `out/fader/out1-peq1-bypass-series`
- `out/fader/out1-peq9-frequency-series`
- `out/fader/out1-peq9-q-series`
- `out/fader/out1-peq9-gain-series`
- `out/fader/out1-peq9-type-series`
- `out/fader/out1-peq9-bypass-series`

Observed write template:

`00 01 0A 33 CC BB GG GG FF FF QQ TT BY`

Fields:

- `CC`: output channel index. Out1 is `0x04`.
- `BB`: PEQ band index. PEQ1 is `0x00`, PEQ9 is `0x08`.
- `GG GG`: gain, little-endian u16 at payload offsets 6..7.
- `FF FF`: frequency, little-endian u16 at payload offsets 8..9.
- `QQ`: Q raw byte at payload offset 10.
- `TT`: filter type byte at payload offset 11.
- `BY`: bypass byte at payload offset 12.

Current capture evidence:

| Capture | Series | Changing offsets | Last payload |
| --- | ---: | --- | --- |
| Out1 PEQ1 Frequency | 4 | `[8, 9]` | `00 01 0A 33 04 00 78 00 2C 01 23 00 00` |
| Out1 PEQ1 Q | 7 | `[10]` | `00 01 0A 33 04 00 78 00 2C 01 64 00 00` |
| Out1 PEQ1 Gain | 28 | `[6]` | `00 01 0A 33 04 00 F0 00 2C 01 64 00 00` |
| Out1 PEQ1 Type | 2 | `[]` | `00 01 0A 33 04 00 F0 00 2C 01 0A 01 00` |
| Out1 PEQ1 Bypass | 3 | `[12]` | `00 01 0A 33 04 00 F0 00 2C 01 0A 01 01` |
| Out1 PEQ9 Frequency | 13 | `[8, 9]` | `00 01 0A 33 04 08 78 00 00 00 23 00 00` |
| Out1 PEQ9 Q | 12 | `[10]` | `00 01 0A 33 04 08 78 00 00 00 00 00 00` |
| Out1 PEQ9 Gain | 24 | `[6]` | `00 01 0A 33 04 08 00 00 00 00 00 00 00` |
| Out1 PEQ9 Type | 2 | `[]` | `00 01 0A 33 04 08 00 00 00 00 23 08 00` |
| Out1 PEQ9 Bypass | 3 | `[12]` | `00 01 0A 33 04 08 00 00 00 00 23 08 01` |

Notes:

- The short gain movements only changed the low byte in these captures; older captures and the 0x33 layout confirm gain is still u16le.
- Type endpoint captures can have empty `changingOffsets` lists when the saved series contains duplicated same payloads; the last payload still pins payload offset 11.
