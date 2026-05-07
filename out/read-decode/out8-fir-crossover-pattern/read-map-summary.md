# Out8 FIR Crossover Readback Map

Script:

- `script-example/395-read-decode-out8-fir-crossover-pattern.dspd`

The script wrote a GUI-observed baseline, then isolated Type/Window, Frequency, Frequency high-byte, Taps, and all-fields patterns. It restored the GUI baseline afterwards.

## Write Patterns

Baseline:

`00 01 0A 4B 0B 00 06 6E 00 2C 01 08 00`

Patterns:

- Type/Window: `00 01 0A 4B 0B 03 0A 6E 00 2C 01 08 00`
- Frequency low-byte: `00 01 0A 4B 0B 00 06 5A 00 A5 00 08 00`
- Frequency high-byte: `00 01 0A 4B 0B 00 06 2B 01 2C 01 08 00`
- Taps: `00 01 0A 4B 0B 00 06 6E 00 2C 01 18 00`
- All fields: `00 01 0A 4B 0B 02 05 5A 00 A5 00 18 00`

## Readback Locations

| Field | Readback Location | Type | Observed Change |
| --- | --- | --- | --- |
| FIR type | `0x1B:0x07` | u8 | `00 -> 03/02` |
| FIR window | `0x1B:0x08` | u8 | `06 -> 0A/05` |
| FIR HighPass frequency | `0x1B:0x09` | u16le | `6E 00 -> 5A 00` and `6E 00 -> 2B 01` |
| FIR LowPass frequency | `0x1B:0x0B` | u16le | `2C 01 -> A5 00` |
| FIR taps raw | `0x1B:0x0D` | u16le | `08 00 -> 18 00` |

## Record Offsets

For Out8, output record base is absolute config offset `1324`.

| Field | Absolute Offset | Record Offset |
| --- | ---: | ---: |
| FIR type | 1352 | 28 |
| FIR window | 1353 | 29 |
| FIR HighPass frequency | 1354 | 30 |
| FIR LowPass frequency | 1356 | 32 |
| FIR taps raw | 1358 | 34 |
