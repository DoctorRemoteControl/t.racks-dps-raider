# Out1 IIR Crossover Readback Map

Script: `script-example/368-read-decode-out1-iir-crossover-pattern.dspd`

The script wrote a baseline, then an HP-only pattern, an LP-only pattern, and both patterns. It restored the baseline afterwards.

## Write Patterns

Baseline:

- HP: `00 01 06 32 04 00 00 00 0A`
- LP: `00 01 06 31 04 2C 01 00 0A`

Patterns:

- HP: `00 01 06 32 04 5A 00 14 0A`
- LP: `00 01 06 31 04 A5 00 05 0A`

## Readback Locations

| Field | Readback Location | Type | Observed Change |
| --- | --- | --- | --- |
| HighPass frequency | `0x0B:0x2B` | u16le | `00 00 -> 5A 00` |
| LowPass frequency | `0x0B:0x2D` | u16le | `2C 01 -> A5 00` |
| HighPass state/slope | `0x0B:0x2F` | u8 | `00 -> 14` |
| LowPass state/slope | `0x0B:0x30` | u8 | `00 -> 05` |

## Raw Diffs

HP-only:

`block 0B: 2B:00->5A 2F:00->14`

LP-only:

`block 0B: 2D:2C->A5 2E:01->00 30:00->05`

Both:

`block 0B: 2B:00->5A 2D:2C->A5 2E:01->00 2F:00->14 30:00->05`

## Storage Formula

For Out1, output base is absolute config offset `568`.

| Field | Absolute Offset | Formula |
| --- | ---: | --- |
| HighPass frequency | 588 | `output_base + 20` |
| LowPass frequency | 590 | `output_base + 22` |
| HighPass state/slope | 592 | `output_base + 24` |
| LowPass state/slope | 593 | `output_base + 25` |

State/slope `0x00` is bypass. State/slope `0x01..0x14` maps to `crossover_slopes` in enum order.
