# Out1 PEQ Readback Map

Script: `script-example/348-read-decode-out1-peq-allbands-pattern.dspd`

The script wrote a baseline, then a distinct value pattern for all Out1 PEQ bands, then two bypass masks. It restored the Out1 PEQ baseline afterwards.

## Value Record Layout

Each output PEQ value record is six bytes:

`gain_u16le frequency_u16le q_u8 type_u8`

Out1 PEQ records begin at absolute config offset 604 (`outputBase(Out1) + 36`). The 50-byte read-block payload framing means the readback crosses from block `0x0C` into block `0x0D`.

| Band | Record Start | Gain | Frequency | Q | Type |
| --- | --- | --- | --- | --- | --- |
| PEQ1 | `0x0C:0x09` | `0x0C:0x09` | `0x0C:0x0B` | `0x0C:0x0D` | `0x0C:0x0E` |
| PEQ2 | `0x0C:0x0F` | `0x0C:0x0F` | `0x0C:0x11` | `0x0C:0x13` | `0x0C:0x14` |
| PEQ3 | `0x0C:0x15` | `0x0C:0x15` | `0x0C:0x17` | `0x0C:0x19` | `0x0C:0x1A` |
| PEQ4 | `0x0C:0x1B` | `0x0C:0x1B` | `0x0C:0x1D` | `0x0C:0x1F` | `0x0C:0x20` |
| PEQ5 | `0x0C:0x21` | `0x0C:0x21` | `0x0C:0x23` | `0x0C:0x25` | `0x0C:0x26` |
| PEQ6 | `0x0C:0x27` | `0x0C:0x27` | `0x0C:0x29` | `0x0C:0x2B` | `0x0C:0x2C` |
| PEQ7 | `0x0C:0x2D` | `0x0C:0x2D` | `0x0C:0x2F` | `0x0C:0x31` | `0x0C:0x32` |
| PEQ8 | `0x0C:0x33` | `0x0C:0x33` | `0x0C:0x35` | `0x0D:0x05` | `0x0D:0x06` |
| PEQ9 | `0x0D:0x07` | `0x0D:0x07` | `0x0D:0x09` | `0x0D:0x0B` | `0x0D:0x0C` |

## Observed Diffs

Baseline to value pattern changed only value bytes in blocks `0x0C` and `0x0D`.

`0x0C`: `09:78->5A 0B:1F->0A 0D:23->0A 0E:00->01 0F:78->69 11:3F->2D 13:23->14 14:00->02 15:78->7B 17:5F->50 19:23->1E 1A:00->03 1B:78->87 1D:7F->73 1F:23->28 20:00->04 21:78->96 23:9E->96 25:23->32 26:00->05 27:78->A5 29:BE->B9 2B:23->3C 2C:00->06 2D:78->B4 2F:DE->DC 31:23->46 32:00->07 33:78->C3 35:FD->FF`

`0x0D`: `05:23->50 06:00->08 07:78->D2 09:1D->22 0B:23->5A 0C:00->01`

## Bypass Mask

Out1 output PEQ bypass readback is not stored inside the six-byte value records. It uses a two-byte bitmask:

| Field | Readback Byte | Meaning |
| --- | --- | --- |
| `bypass_mask_low_u8` | `0x1C:0x31` | PEQ1..PEQ8, bit0 = PEQ1 |
| `bypass_mask_high_u8` | `0x1C:0x32` | PEQ9, bit0 = PEQ9 |

Bypass pattern PEQ2/4/6/8 changed `0x1C:0x31` from `00` to `AA`. Bypass pattern PEQ9-only changed `0x1C:0x32` from `00` to `01`.

This supersedes the older partial Out1 PEQ read offsets that placed PEQ1 gain/frequency/Q at `0x0C:0x05/0x07/0x09`.
