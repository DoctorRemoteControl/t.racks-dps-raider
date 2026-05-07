# Output Limiter Readback Map

Capture date: 2026-05-07

Script: `script-example/277-read-decode-all-limit-minmax-allblocks.dspd`

The script wrote GUI-valid min and max states for command `0x3F` on output channels
`0x04..0x0B`, then read blocks `0x00..0x1C` before and after each write.

Min state:

```text
ratio=0 attack=0 release=9 knee=0 threshold=0
```

Max state:

```text
ratio=15 attack=998 release=2999 knee=12 threshold=220
```

Readback record order:

```text
RR 00 AA AA LL LL KK 00 TT TT
```

Where:

- `RR` = ratio enum
- `AA AA` = attack u16le
- `LL LL` = release u16le
- `KK` = knee enum/value
- `TT TT` = threshold u16le

Locations:

| Output | Block | Ratio | Attack | Release | Knee | Threshold |
| --- | ---: | ---: | ---: | ---: | ---: | ---: |
| Out1 | `0x0D` | `0x0D` | `0x0F` | `0x11` | `0x13` | `0x15` |
| Out2 | `0x0F` | `0x15` | `0x17` | `0x19` | `0x1B` | `0x1D` |
| Out3 | `0x11` | `0x1D` | `0x1F` | `0x21` | `0x23` | `0x25` |
| Out4 | `0x13` | `0x25` | `0x27` | `0x29` | `0x2B` | `0x2D` |
| Out5 | `0x15` | `0x2D` | `0x2F` | `0x31` | `0x33` | `0x35` |
| Out6 | `0x17` | `0x35` | - | - | - | - |
| Out6 | `0x18` | - | `0x05` | `0x07` | `0x09` | `0x0B` |
| Out7 | `0x1A` | `0x0B` | `0x0D` | `0x0F` | `0x11` | `0x13` |
| Out8 | `0x1C` | `0x13` | `0x15` | `0x17` | `0x19` | `0x1B` |

Out6 crosses a block boundary. Its ratio byte is at the final payload offset of
block `0x17`; attack..threshold continue at the first usable data offset (`0x05`)
of block `0x18`.
