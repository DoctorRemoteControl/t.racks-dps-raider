# Delay Readback Map

Capture date: 2026-05-07

Search script: `script-example/282-read-decode-all-delay-minmax-allblocks.dspd`

Confirmation script: `script-example/283-read-decode-all-delay-20ms-target-blocks.dspd`

Write command:

```text
00 01 04 38 CC DD DD
```

Where:

- `CC` = channel index
- `DD DD` = delay raw value, u16le

Value anchors:

```text
0 raw     = 0.000 ms
1920 raw  = 20.000 ms
65280 raw = 680.000 ms
ms = raw / 96.0
```

The all-block min/max search used `0 -> 65280`. Because `65280 = 0xFF00`,
only the high byte changed in that search. The target-block confirmation used
`0 -> 1920 = 0x0780`, which changed both bytes and confirms the exact u16le
start offsets below.

Locations:

| Channel | Channel byte | Block | Delay u16le start | 20ms changed bytes | 680ms changed byte |
| --- | ---: | ---: | ---: | --- | ---: |
| InA | `0x00` | `0x03` | `0x05` | `0x05, 0x06` | `0x06` |
| InB | `0x01` | `0x05` | `0x2B` | `0x2B, 0x2C` | `0x2C` |
| InC | `0x02` | `0x08` | `0x1F` | `0x1F, 0x20` | `0x20` |
| InD | `0x03` | `0x0B` | `0x13` | `0x13, 0x14` | `0x14` |
| Out1 | `0x04` | `0x0D` | `0x1B` | `0x1B, 0x1C` | `0x1C` |
| Out2 | `0x05` | `0x0F` | `0x23` | `0x23, 0x24` | `0x24` |
| Out3 | `0x06` | `0x11` | `0x2B` | `0x2B, 0x2C` | `0x2C` |
| Out4 | `0x07` | `0x13` | `0x33` | `0x33, 0x34` | `0x34` |
| Out5 | `0x08` | `0x16` | `0x09` | `0x09, 0x0A` | `0x0A` |
| Out6 | `0x09` | `0x18` | `0x11` | `0x11, 0x12` | `0x12` |
| Out7 | `0x0A` | `0x1A` | `0x19` | `0x19, 0x1A` | `0x1A` |
| Out8 | `0x0B` | `0x1C` | `0x21` | `0x21, 0x22` | `0x22` |

Conclusion:

Delay write and readback are now observed for every FIR408 channel. The old
inherited read offsets were stale for several channels and should not be used.
