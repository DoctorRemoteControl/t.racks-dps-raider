# InD PEQ Readback Map

Source:

- `script-example/337-read-decode-ind-peq-allbands-pattern.dspd`
- output folder: `out/read-decode/ind-peq-allbands-pattern`

The script wrote a known baseline for InD PEQ1..PEQ8, then a unique value pattern across all eight bands. Comparing the saved readback dumps showed that only blocks `0x09` and `0x0A` changed for PEQ values:

`changed-blocks=9,10`

It then wrote an alternating bypass pattern with PEQ2/4/6/8 bypass enabled. Block `0x1C` offset `0x2F` changed from `0x00` to `0xAA`, confirming a linear bitmask with PEQ1 at bit0 and PEQ8 at bit7.

Offsets below use the existing library convention: zero-based offset inside `readBlock(...).payload`, including the five-byte read response header.

| Band | Gain u16le | Frequency u16le | Q u8 | Type u8 |
| --- | --- | --- | --- | --- |
| PEQ1 | `0x09:49` | `0x09:51` | `0x09:53` | `0x09:54` |
| PEQ2 | `0x0A:5` | `0x0A:7` | `0x0A:9` | `0x0A:10` |
| PEQ3 | `0x0A:11` | `0x0A:13` | `0x0A:15` | `0x0A:16` |
| PEQ4 | `0x0A:17` | `0x0A:19` | `0x0A:21` | `0x0A:22` |
| PEQ5 | `0x0A:23` | `0x0A:25` | `0x0A:27` | `0x0A:28` |
| PEQ6 | `0x0A:29` | `0x0A:31` | `0x0A:33` | `0x0A:34` |
| PEQ7 | `0x0A:35` | `0x0A:37` | `0x0A:39` | `0x0A:40` |
| PEQ8 | `0x0A:41` | `0x0A:43` | `0x0A:45` | `0x0A:46` |

Bypass:

- storage: block `0x1C`, offset `47` (`0x2F`)
- encoding: `bitmask_u8`
- bit order: bit0 PEQ1, bit1 PEQ2, bit2 PEQ3, bit3 PEQ4, bit4 PEQ5, bit5 PEQ6, bit6 PEQ7, bit7 PEQ8
- proof pattern: PEQ2/4/6/8 on produced `0xAA`

Important correction:

- The older InD PEQ read anchors at `0x0A:19` for PEQ1 gain and `0x0B:11` for PEQ8 gain are stale for FIR408 and are superseded by this pattern run.
