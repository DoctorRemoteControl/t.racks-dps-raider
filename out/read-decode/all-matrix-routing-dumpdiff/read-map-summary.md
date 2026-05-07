# Matrix Routing Readback Map

Capture date: 2026-05-07

Script: `script-example/290-read-decode-all-matrix-routing-dumpdiff.dspd`

Direct endpoint confirmation:

- `script-example/184-read-decode-out1-matrix-routing-allblocks.dspd`
- `script-example/185-read-decode-out8-matrix-routing-allblocks.dspd`

Write command:

```text
00 01 03 3A OO MM
```

Where:

- `OO` = output channel (`Out1=0x04` ... `Out8=0x0B`)
- `MM` = complete input route bitmask (`InA=0x01`, `InB=0x02`, `InC=0x04`, `InD=0x08`)

Fresh sequential GUI captures proved that `MM` is the full 4-bit mask state, so
combinations are valid: `0x03` means InA+InB and `0x09` means InA+InD.

The dump diff wrote all outputs from `InA=0x01` to `InD=0x08`.

Locations:

| Output | Channel byte | Block | Route byte |
| --- | ---: | ---: | ---: |
| Out1 | `0x04` | `0x0B` | `0x1F` |
| Out2 | `0x05` | `0x0D` | `0x27` |
| Out3 | `0x06` | `0x0F` | `0x2F` |
| Out4 | `0x07` | `0x12` | `0x05` |
| Out5 | `0x08` | `0x14` | `0x0D` |
| Out6 | `0x09` | `0x16` | `0x15` |
| Out7 | `0x0A` | `0x18` | `0x1D` |
| Out8 | `0x0B` | `0x1A` | `0x25` |

Out1 direct scan confirmed block `0x0B` offset `0x1F`.
Out8 direct scan confirmed block `0x1A` offset `0x25`.

The route byte is followed by a zero pad byte, then four matrix gain u16le values
in input order `InA`, `InB`, `InC`, `InD`.
