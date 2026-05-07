# Output IIR Crossover Out1..Out8 Map

Basis:

- Out1 GUI capture: `out/fader/out1-iir-crossover-field-map-summary.md`
- Out1 readback: `out/read-decode/out1-iir-crossover-pattern/read-map-summary.md`
- Out8 GUI capture: `out/fader/out8-iir-crossover-field-map-summary.md`
- Out8 readback: `out/read-decode/out8-iir-crossover-pattern/read-map-summary.md`

## Write Map

All output IIR crossover writes use the same 9-byte payload shape:

- HighPass: `00 01 06 32 <output_channel> <frequency_lo> <frequency_hi> <state_or_slope> 0A`
- LowPass: `00 01 06 31 <output_channel> <frequency_lo> <frequency_hi> <state_or_slope> 0A`

| Output | Channel Byte |
| --- | --- |
| Out1 | `0x04` |
| Out2 | `0x05` |
| Out3 | `0x06` |
| Out4 | `0x07` |
| Out5 | `0x08` |
| Out6 | `0x09` |
| Out7 | `0x0A` |
| Out8 | `0x0B` |

Payload offsets:

| Field | Offset | Type |
| --- | ---: | --- |
| output channel | `4` | u8 |
| frequency | `5..6` | u16le |
| state/slope | `7` | u8 |
| tail/context | `8` | constant `0x0A` |

State/slope `0x00` is bypass. State/slope `0x01..0x14` maps to `crossover_slopes` in enum order.

## Read Formula

Output record base:

`output_base = 568 + (output_index_zero_based * 108)`

Field absolute offsets:

| Field | Formula |
| --- | --- |
| HighPass frequency | `output_base + 20` |
| LowPass frequency | `output_base + 22` |
| HighPass state/slope | `output_base + 24` |
| LowPass state/slope | `output_base + 25` |

Block dump coordinate:

`block = floor(absolute_offset / 50)`

`payload_offset = (absolute_offset % 50) + 5`

## Read Map

| Output | HP Freq | LP Freq | HP State/Slope | LP State/Slope | Status |
| --- | --- | --- | --- | --- | --- |
| Out1 | `0x0B:0x2B` | `0x0B:0x2D` | `0x0B:0x2F` | `0x0B:0x30` | observed |
| Out2 | `0x0D:0x33` | `0x0D:0x35` | `0x0E:0x05` | `0x0E:0x06` | derived |
| Out3 | `0x10:0x09` | `0x10:0x0B` | `0x10:0x0D` | `0x10:0x0E` | derived |
| Out4 | `0x12:0x11` | `0x12:0x13` | `0x12:0x15` | `0x12:0x16` | derived |
| Out5 | `0x14:0x19` | `0x14:0x1B` | `0x14:0x1D` | `0x14:0x1E` | derived |
| Out6 | `0x16:0x21` | `0x16:0x23` | `0x16:0x25` | `0x16:0x26` | derived |
| Out7 | `0x18:0x29` | `0x18:0x2B` | `0x18:0x2D` | `0x18:0x2E` | derived |
| Out8 | `0x1A:0x31` | `0x1A:0x33` | `0x1A:0x35` | `0x1A:0x36` | observed |

Out2 crosses a block boundary: its two frequency fields are at the end of block `0x0D`, while state/slope bytes begin at block `0x0E`.
