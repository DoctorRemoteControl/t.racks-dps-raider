# Input Compressor Readback Map

Capture date: 2026-05-07

Script: `script-example/265-read-decode-input-comp-minmax-allblocks.dspd`

The script wrote GUI-valid min and max states for command `0x30` on input channels
`0x00..0x03`, then read blocks `0x00..0x1C` before and after each write.

Min state:

```text
ratio=0 attack=0 release=9 knee=0 threshold=0 frequency=0 type=0 context=0x23
```

Max state:

```text
ratio=15 attack=998 release=2999 knee=12 threshold=220 frequency=300 type=9 context=0x23
```

Readback record order:

```text
RR 00 AA AA LL LL KK 00 TT TT FF FF XX YY
```

Where:

- `RR` = ratio enum
- `AA AA` = attack u16le
- `LL LL` = release u16le
- `KK` = knee enum/value
- `TT TT` = threshold u16le
- `FF FF` = frequency u16le
- `XX` = type_context_u8, unchanged at `0x23` in this test
- `YY` = type enum

Locations:

| Input | Block | Ratio | Attack | Release | Knee | Threshold | Freq | Type context | Type |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| InA | `0x02` | `0x21` | `0x23` | `0x25` | `0x27` | `0x29` | `0x2B` | `0x2D` | `0x2E` |
| InB | `0x05` | `0x15` | `0x17` | `0x19` | `0x1B` | `0x1D` | `0x1F` | `0x21` | `0x22` |
| InC | `0x08` | `0x09` | `0x0B` | `0x0D` | `0x0F` | `0x11` | `0x13` | `0x15` | `0x16` |
| InD | `0x0A` | `0x2F` | `0x31` | `0x33` | `0x35` | - | - | - | - |
| InD | `0x0B` | - | - | - | - | `0x05` | `0x07` | `0x09` | `0x0A` |

InD crosses a block boundary. Its ratio..knee bytes are at the end of block `0x0A`;
threshold..type continue at the first usable data offset (`0x05`) of block `0x0B`.
