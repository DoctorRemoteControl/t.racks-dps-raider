# Out8 External FIR Import Summary

Fresh scripts:

- `script-example/396-read-out8-external-fir-before.dspd`
- `script-example/397-auto-capture-out8-external-fir-impulse-load.dspd`
- `script-example/398-read-out8-external-fir-after-and-probe.dspd`

Test file:

`fir-test-files/fir_512_impulse_first.txt`

## GUI Capture

Out8 External FIR import produced:

- `0x4B` writes: 0
- `0x4F` writes: 0
- `0x4E` writes: 43
- `0x5B` writes: 1

Out8 uses selector/channel byte `0x0B` for both chunk upload and name write.

First coefficient chunk:

`00 01 35 4E 0B 00 00 02 3F 80 00 00 ...`

Decoded:

- selector/channel `0x0B` = Out8
- chunk index `0x00`
- tap count bytes `00 02` = 512
- first coefficient upload prefix `3F 80 00 00` = float32 big-endian `1.0`

Last chunk observed:

`00 01 25 4E 0B 2A 00 02 ...`

The upload order for this Out8 capture was chunk index `0x00..0x2A`.

Name write:

`00 01 0A 5B 0B 66 69 72 5F 35 31 32 5F`

Decoded name prefix: `fir_512_`.

## Config Diff

Before/after config diff:

| Absolute Offset | Meaning | Before | After |
| ---: | --- | --- | --- |
| 1352 | Out8 FIR type | `0x00` | `0x04` |
| 1358..1359 | Out8 External FIR taps | `08 00` | `00 02` |
| 1548..1555 | Out8 FIR file name | `Out8File` | `fir_512_` |

Important: in generated FIR mode, the taps field stores a raw index (`0x0008` = 512 taps). In External FIR mode, the same output-record field stores the actual tap count (`0x0200` = 512 taps).

## 0x56 Probe

After Out8 import, selectors `0x00..0x0B` were probed at offsets `0,13,26,39,52,255`.

All sampled selectors returned FF-filled data. This matches the fresh Out1 output-channel External FIR observation: the currently known `0x56` coefficient/window readback path does not expose freshly imported output-channel External FIR data, even though GUI upload and config name/type/taps fields are confirmed.
