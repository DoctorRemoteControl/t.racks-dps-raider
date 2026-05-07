# InA PEQ Readback Map

Source:

- `script-example/326-read-decode-ina-peq-allbands-pattern.dspd`
- output folder: `out/read-decode/ina-peq-allbands-pattern`

The script wrote a known baseline for InA PEQ1..PEQ8, then a unique value pattern across all eight bands. Comparing the saved readback dumps showed that only blocks `0x01` and `0x02` changed for PEQ values:

`changed-blocks=1,2`

It then wrote an alternating bypass pattern with PEQ2/4/6/8 bypass enabled. Block `0x1C` offset `0x29` changed from `0x00` to `0xAA`, confirming a linear bitmask with PEQ1 at bit0 and PEQ8 at bit7.

Offsets below use the existing library convention: zero-based offset inside `readBlock(...).payload`, including the five-byte read response header.

| Band | Gain u16le | Frequency u16le | Q u8 | Type u8 |
| --- | --- | --- | --- | --- |
| PEQ1 | `0x01:35` | `0x01:37` | `0x01:39` | `0x01:40` |
| PEQ2 | `0x01:41` | `0x01:43` | `0x01:45` | `0x01:46` |
| PEQ3 | `0x01:47` | `0x01:49` | `0x01:51` | `0x01:52` |
| PEQ4 | `0x01:53` | `0x02:5` | `0x02:7` | `0x02:8` |
| PEQ5 | `0x02:9` | `0x02:11` | `0x02:13` | `0x02:14` |
| PEQ6 | `0x02:15` | `0x02:17` | `0x02:19` | `0x02:20` |
| PEQ7 | `0x02:21` | `0x02:23` | `0x02:25` | `0x02:26` |
| PEQ8 | `0x02:27` | `0x02:29` | `0x02:31` | `0x02:32` |

Bypass:

- storage: block `0x1C`, offset `41` (`0x29`)
- encoding: `bitmask_u8`
- bit order: bit0 PEQ1, bit1 PEQ2, bit2 PEQ3, bit3 PEQ4, bit4 PEQ5, bit5 PEQ6, bit6 PEQ7, bit7 PEQ8
- proof pattern: PEQ2/4/6/8 on produced `0xAA`

Important correction:

- Older InA PEQ read hypotheses that placed PEQ8 at block `0x02` offset `41` and bypass at block `0x1C` offset `17` are stale for FIR408 and are superseded by this pattern run.
