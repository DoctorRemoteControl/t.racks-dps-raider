# Out8 PEQ Readback Map

Script: `script-example/359-read-decode-out8-peq-allbands-pattern.dspd`

The script wrote a baseline, then a distinct value pattern for all Out8 PEQ bands, then two bypass masks. It restored the Out8 PEQ baseline afterwards.

## Value Record Layout

Each output PEQ value record is six bytes:

`gain_u16le frequency_u16le q_u8 type_u8`

Out8 PEQ records begin at absolute config offset 1360 (`outputBase(Out8) + 36`). The readback crosses from block `0x1B` into block `0x1C`.

| Band | Record Start | Gain | Frequency | Q | Type |
| --- | --- | --- | --- | --- | --- |
| PEQ1 | `0x1B:0x0F` | `0x1B:0x0F` | `0x1B:0x11` | `0x1B:0x13` | `0x1B:0x14` |
| PEQ2 | `0x1B:0x15` | `0x1B:0x15` | `0x1B:0x17` | `0x1B:0x19` | `0x1B:0x1A` |
| PEQ3 | `0x1B:0x1B` | `0x1B:0x1B` | `0x1B:0x1D` | `0x1B:0x1F` | `0x1B:0x20` |
| PEQ4 | `0x1B:0x21` | `0x1B:0x21` | `0x1B:0x23` | `0x1B:0x25` | `0x1B:0x26` |
| PEQ5 | `0x1B:0x27` | `0x1B:0x27` | `0x1B:0x29` | `0x1B:0x2B` | `0x1B:0x2C` |
| PEQ6 | `0x1B:0x2D` | `0x1B:0x2D` | `0x1B:0x2F` | `0x1B:0x31` | `0x1B:0x32` |
| PEQ7 | `0x1B:0x33` | `0x1B:0x33` | `0x1B:0x35` | `0x1C:0x05` | `0x1C:0x06` |
| PEQ8 | `0x1C:0x07` | `0x1C:0x07` | `0x1C:0x09` | `0x1C:0x0B` | `0x1C:0x0C` |
| PEQ9 | `0x1C:0x0D` | `0x1C:0x0D` | `0x1C:0x0F` | `0x1C:0x11` | `0x1C:0x12` |

## Observed Diffs

Baseline to value pattern changed only value bytes in blocks `0x1B` and `0x1C`.

`0x1B`: `0F:78->5A 11:1F->0A 13:23->0A 14:00->01 15:78->69 17:3F->2D 19:23->14 1A:00->02 1B:78->7B 1D:5F->50 1F:23->1E 20:00->03 21:78->87 23:7F->73 25:23->28 26:00->04 27:78->96 29:9E->96 2B:23->32 2C:00->05 2D:78->A5 2F:BE->B9 31:23->3C 32:00->06 33:78->B4 35:DE->DC`

`0x1C`: `05:23->46 06:00->07 07:78->C3 09:FD->FF 0B:23->50 0C:00->08 0D:78->D2 0F:1D->22 11:23->5A 12:00->01`

## Bypass Mask

Out8 output PEQ bypass readback is not stored inside the six-byte value records. It uses a two-byte bitmask:

| Field | Readback Byte | Meaning |
| --- | --- | --- |
| `bypass_mask_low_u8` | `0x1D:0x0D` | PEQ1..PEQ8, bit0 = PEQ1 |
| `bypass_mask_high_u8` | `0x1D:0x0E` | PEQ9, bit0 = PEQ9 |

Bypass pattern PEQ2/4/6/8 changed `0x1D:0x0D` from `00` to `AA`. Bypass pattern PEQ9-only changed `0x1D:0x0E` from `00` to `01`.

This confirms the output PEQ value-record stride predicted from Out1 and confirms that output PEQ bypass masks continue into block `0x1D` for later outputs.
