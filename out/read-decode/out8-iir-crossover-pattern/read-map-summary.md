# Out8 IIR Crossover Readback Map

Script: `script-example/375-read-decode-out8-iir-crossover-pattern.dspd`

The script wrote a baseline, then an HP-only pattern, an LP-only pattern, and both patterns. It restored the baseline afterwards.

## Write Patterns

Baseline:

- HP: `00 01 06 32 0B 00 00 00 0A`
- LP: `00 01 06 31 0B 2C 01 00 0A`

Patterns:

- HP: `00 01 06 32 0B 5A 00 14 0A`
- LP: `00 01 06 31 0B A5 00 05 0A`

## Readback Locations

| Field | Readback Location | Type | Observed Change |
| --- | --- | --- | --- |
| HighPass frequency | `0x1A:0x31` | u16le | `00 00 -> 5A 00` |
| LowPass frequency | `0x1A:0x33` | u16le | `2C 01 -> A5 00` |
| HighPass state/slope | `0x1A:0x35` | u8 | `00 -> 14` |
| LowPass state/slope | `0x1A:0x36` | u8 | `00 -> 05` |

## Raw Diffs

HP-only:

`block 1A: 31:00->5A 35:00->14`

LP-only:

`block 1A: 33:2C->A5 34:01->00 36:00->05`

Both:

`block 1A: 31:00->5A 33:2C->A5 34:01->00 35:00->14 36:00->05`

## Storage Formula

For Out8, output base is absolute config offset `1324`.

| Field | Absolute Offset | Formula |
| --- | ---: | --- |
| HighPass frequency | 1344 | `output_base + 20` |
| LowPass frequency | 1346 | `output_base + 22` |
| HighPass state/slope | 1348 | `output_base + 24` |
| LowPass state/slope | 1349 | `output_base + 25` |

State/slope `0x00` is bypass. State/slope `0x01..0x14` maps to `crossover_slopes` in enum order.
