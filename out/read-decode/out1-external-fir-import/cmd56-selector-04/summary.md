# FIR408 0x56 Read-only Offset Probe

- Read-only probe. No DSP setting write commands are sent.
- Selector: 0x04
- Offsets: `0,13,26,39,52,255`
- Attempts per offset: 1
- Requests: 6
- Data rows: 0, zero rows: 0, partial rows: 0, FF rows: 6, no response rows: 0

| Selector | Offset | Bytes | Non-FF bytes | Non-FF slots | Prefix | Float[0] | Float[1] | Class |
| ---: | ---: | ---: | ---: | ---: | --- | ---: | ---: | --- |
| 0x04 | 0 | 52 | 0 | 0 | `FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF ...` |  |  | ff_empty_or_unavailable |
| 0x04 | 13 | 52 | 0 | 0 | `FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF ...` |  |  | ff_empty_or_unavailable |
| 0x04 | 26 | 52 | 0 | 0 | `FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF ...` |  |  | ff_empty_or_unavailable |
| 0x04 | 39 | 52 | 0 | 0 | `FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF ...` |  |  | ff_empty_or_unavailable |
| 0x04 | 52 | 52 | 0 | 0 | `FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF ...` |  |  | ff_empty_or_unavailable |
| 0x04 | 255 | 52 | 0 | 0 | `FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF ...` |  |  | ff_empty_or_unavailable |