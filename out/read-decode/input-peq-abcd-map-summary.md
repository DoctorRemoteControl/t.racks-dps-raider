# Input PEQ A/B/C/D Readback Map

Sources:

- InA observed: `script-example/326-read-decode-ina-peq-allbands-pattern.dspd`
- InD observed: `script-example/337-read-decode-ind-peq-allbands-pattern.dspd`
- InB/InC predicted from the observed InA/InD input stride

Offset convention:

- Offsets are zero-based inside `readBlock(...).payload`, including the five-byte read response header.
- Each read block carries 50 data bytes after that header.

Stride:

- InA PEQ1 gain starts at absolute data address `80`.
- InD PEQ1 gain starts at absolute data address `494`.
- `494 - 80 = 414 = 3 * 138`, so the input PEQ stride is `138` bytes.

## Value Records

Each PEQ record is:

`gain_u16le + frequency_u16le + q_raw_u8 + filter_type_u8`

### InA Observed

| Band | Gain | Frequency | Q | Type |
| --- | --- | --- | --- | --- |
| PEQ1 | `0x01:35` | `0x01:37` | `0x01:39` | `0x01:40` |
| PEQ2 | `0x01:41` | `0x01:43` | `0x01:45` | `0x01:46` |
| PEQ3 | `0x01:47` | `0x01:49` | `0x01:51` | `0x01:52` |
| PEQ4 | `0x01:53` | `0x02:5` | `0x02:7` | `0x02:8` |
| PEQ5 | `0x02:9` | `0x02:11` | `0x02:13` | `0x02:14` |
| PEQ6 | `0x02:15` | `0x02:17` | `0x02:19` | `0x02:20` |
| PEQ7 | `0x02:21` | `0x02:23` | `0x02:25` | `0x02:26` |
| PEQ8 | `0x02:27` | `0x02:29` | `0x02:31` | `0x02:32` |

### InB Predicted

| Band | Gain | Frequency | Q | Type |
| --- | --- | --- | --- | --- |
| PEQ1 | `0x04:23` | `0x04:25` | `0x04:27` | `0x04:28` |
| PEQ2 | `0x04:29` | `0x04:31` | `0x04:33` | `0x04:34` |
| PEQ3 | `0x04:35` | `0x04:37` | `0x04:39` | `0x04:40` |
| PEQ4 | `0x04:41` | `0x04:43` | `0x04:45` | `0x04:46` |
| PEQ5 | `0x04:47` | `0x04:49` | `0x04:51` | `0x04:52` |
| PEQ6 | `0x04:53` | `0x05:5` | `0x05:7` | `0x05:8` |
| PEQ7 | `0x05:9` | `0x05:11` | `0x05:13` | `0x05:14` |
| PEQ8 | `0x05:15` | `0x05:17` | `0x05:19` | `0x05:20` |

### InC Predicted

| Band | Gain | Frequency | Q | Type |
| --- | --- | --- | --- | --- |
| PEQ1 | `0x07:11` | `0x07:13` | `0x07:15` | `0x07:16` |
| PEQ2 | `0x07:17` | `0x07:19` | `0x07:21` | `0x07:22` |
| PEQ3 | `0x07:23` | `0x07:25` | `0x07:27` | `0x07:28` |
| PEQ4 | `0x07:29` | `0x07:31` | `0x07:33` | `0x07:34` |
| PEQ5 | `0x07:35` | `0x07:37` | `0x07:39` | `0x07:40` |
| PEQ6 | `0x07:41` | `0x07:43` | `0x07:45` | `0x07:46` |
| PEQ7 | `0x07:47` | `0x07:49` | `0x07:51` | `0x07:52` |
| PEQ8 | `0x07:53` | `0x08:5` | `0x08:7` | `0x08:8` |

### InD Observed

| Band | Gain | Frequency | Q | Type |
| --- | --- | --- | --- | --- |
| PEQ1 | `0x09:49` | `0x09:51` | `0x09:53` | `0x09:54` |
| PEQ2 | `0x0A:5` | `0x0A:7` | `0x0A:9` | `0x0A:10` |
| PEQ3 | `0x0A:11` | `0x0A:13` | `0x0A:15` | `0x0A:16` |
| PEQ4 | `0x0A:17` | `0x0A:19` | `0x0A:21` | `0x0A:22` |
| PEQ5 | `0x0A:23` | `0x0A:25` | `0x0A:27` | `0x0A:28` |
| PEQ6 | `0x0A:29` | `0x0A:31` | `0x0A:33` | `0x0A:34` |
| PEQ7 | `0x0A:35` | `0x0A:37` | `0x0A:39` | `0x0A:40` |
| PEQ8 | `0x0A:41` | `0x0A:43` | `0x0A:45` | `0x0A:46` |

## Bypass Masks

Bypass is a linear `bitmask_u8` where bit0 is PEQ1 and bit7 is PEQ8.

| Input | Block:Offset | Status |
| --- | --- | --- |
| InA | `0x1C:41` | observed |
| InB | `0x1C:43` | predicted from observed stride |
| InC | `0x1C:45` | predicted from observed stride |
| InD | `0x1C:47` | observed |
