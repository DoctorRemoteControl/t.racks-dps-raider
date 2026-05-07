# InD PEQ GUI Capture Field Map

Fresh GUI captures:

- `out/fader/ind-peq1-frequency-series`
- `out/fader/ind-peq1-q-series`
- `out/fader/ind-peq1-gain-series`
- `out/fader/ind-peq1-type-series`
- `out/fader/ind-peq1-bypass-series`
- `out/fader/ind-peq8-frequency-series`
- `out/fader/ind-peq8-q-series`
- `out/fader/ind-peq8-gain-series`
- `out/fader/ind-peq8-type-series`
- `out/fader/ind-peq8-bypass-series`

Observed write template:

`00 01 0A 33 CC BB GG GG FF FF QQ TT BY`

Fields:

- `CC`: input channel index. InD is `0x03`.
- `BB`: PEQ band index. PEQ1 is `0x00`, PEQ8 is `0x07`.
- `GG GG`: gain, little-endian u16 at payload offsets 6..7.
- `FF FF`: frequency, little-endian u16 at payload offsets 8..9.
- `QQ`: Q raw byte at payload offset 10.
- `TT`: filter type byte at payload offset 11.
- `BY`: bypass byte at payload offset 12.

Current capture evidence:

| Capture | Series | Changing offsets | Last payload |
| --- | ---: | --- | --- |
| InD PEQ1 Frequency | 3 | `[8, 9]` | `00 01 0A 33 03 00 78 00 2C 01 23 00 00` |
| InD PEQ1 Q | 4 | `[10]` | `00 01 0A 33 03 00 78 00 2C 01 64 00 00` |
| InD PEQ1 Gain | 7 | `[6]` | `00 01 0A 33 03 00 00 00 2C 01 64 00 00` |
| InD PEQ1 Type | 2 | `[]` | `00 01 0A 33 03 00 00 00 2C 01 0A 01 00` |
| InD PEQ1 Bypass | 2 | `[12]` | `00 01 0A 33 03 00 00 00 2C 01 0A 01 01` |
| InD PEQ8 Frequency | 4 | `[8, 9]` | `00 01 0A 33 03 07 78 00 00 00 23 00 00` |
| InD PEQ8 Q | 5 | `[10]` | `00 01 0A 33 03 07 78 00 00 00 00 00 00` |
| InD PEQ8 Gain | 6 | `[6]` | `00 01 0A 33 03 07 00 00 00 00 00 00 00` |
| InD PEQ8 Type | 2 | `[]` | `00 01 0A 33 03 07 00 00 00 00 23 08 00` |
| InD PEQ8 Bypass | 1 | `[]` | `00 01 0A 33 03 07 00 00 00 00 23 08 01` |

Notes:

- The short gain movements only changed the low byte in these captures; earlier captures and the 0x33 layout confirm gain is still u16le.
- Type and one-shot bypass endpoint captures can have empty `changingOffsets` lists when the saved series contains one effective payload or duplicated same payloads; the last payload still pins the field byte.
