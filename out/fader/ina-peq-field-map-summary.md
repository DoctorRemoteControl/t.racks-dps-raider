# InA PEQ GUI Capture Field Map

Fresh GUI captures:

- `out/fader/ina-peq1-frequency-series-v2`
- `out/fader/ina-peq1-q-series-v2`
- `out/fader/ina-peq1-gain-series-v2`
- `out/fader/ina-peq1-type-series-v2`
- `out/fader/ina-peq1-bypass-series-v2`
- `out/fader/ina-peq8-frequency-series-v2`
- `out/fader/ina-peq8-q-series-v2`
- `out/fader/ina-peq8-gain-series-v2`
- `out/fader/ina-peq8-type-series-v2`
- `out/fader/ina-peq8-bypass-series-v2`

Observed write template:

`00 01 0A 33 CC BB GG GG FF FF QQ TT BY`

Fields:

- `CC`: input channel index. InA is `0x00`.
- `BB`: PEQ band index. PEQ1 is `0x00`, PEQ8 is `0x07`.
- `GG GG`: gain, little-endian u16 at payload offsets 6..7.
- `FF FF`: frequency, little-endian u16 at payload offsets 8..9.
- `QQ`: Q raw byte at payload offset 10.
- `TT`: filter type byte at payload offset 11.
- `BY`: bypass byte at payload offset 12.

Current capture evidence:

| Capture | Series | Changing offsets | Last payload |
| --- | ---: | --- | --- |
| InA PEQ1 Frequency | 5 | `[8, 9]` | `00 01 0A 33 00 00 78 00 2C 01 23 00 00` |
| InA PEQ1 Q | 5 | `[10]` | `00 01 0A 33 00 00 78 00 18 00 64 00 00` |
| InA PEQ1 Gain | 3 | `[6]` | `00 01 0A 33 00 00 F0 00 18 00 64 00 00` |
| InA PEQ1 Type | 2 | `[]` | `00 01 0A 33 00 00 F0 00 18 00 23 07 00` |
| InA PEQ1 Bypass | 1 | `[]` | `00 01 0A 33 00 00 F0 00 18 00 23 07 01` |
| InA PEQ8 Frequency | 5 | `[8, 9]` | `00 01 0A 33 00 07 78 00 00 00 23 00 00` |
| InA PEQ8 Q | 4 | `[10]` | `00 01 0A 33 00 07 78 00 00 00 64 00 00` |
| InA PEQ8 Gain | 4 | `[6]` | `00 01 0A 33 00 07 F0 00 00 00 64 00 00` |
| InA PEQ8 Type | 2 | `[]` | `00 01 0A 33 00 07 F0 00 00 00 0A 01 00` |
| InA PEQ8 Bypass | 1 | `[]` | `00 01 0A 33 00 07 F0 00 00 00 0A 01 01` |

Notes:

- The short gain movements only changed the low byte in these captures; earlier captures and the 0x33 layout confirm gain is still u16le.
- Type and bypass captures were one-shot endpoint captures, so their `changingOffsets` lists can be empty even though the last payload pins the field byte.
