# Out1 FIR Crossover Readback Map

Scripts:

- `script-example/383-read-decode-out1-fir-crossover-pattern.dspd`
- `script-example/384-read-decode-out1-fir-crossover-frequency-highbytes.dspd`

The scripts wrote a GUI-observed baseline, then isolated Type/Window, Frequency, Taps, and all-fields patterns. They restored the GUI baseline afterwards.

## Write Patterns

Baseline:

`00 01 0A 4B 04 03 0A 91 00 92 00 08 00`

Patterns:

- Type/Window: `00 01 0A 4B 04 01 02 91 00 92 00 08 00`
- Frequency low-byte: `00 01 0A 4B 04 03 0A 5A 00 A5 00 08 00`
- Frequency high-byte: `00 01 0A 4B 04 03 0A 2B 01 2C 01 08 00`
- Taps: `00 01 0A 4B 04 03 0A 91 00 92 00 18 00`
- All fields: `00 01 0A 4B 04 02 05 5A 00 A5 00 18 00`

## Readback Locations

| Field | Readback Location | Type | Observed Change |
| --- | --- | --- | --- |
| FIR type | `0x0B:0x33` | u8 | `03 -> 01/02` |
| FIR window | `0x0B:0x34` | u8 | `0A -> 02/05` |
| FIR HighPass frequency | `0x0B:0x35` | u16le | `91 00 -> 5A 00` and `91 00 -> 2B 01` |
| FIR LowPass frequency | `0x0C:0x05` | u16le | `92 00 -> A5 00` and `92 00 -> 2C 01` |
| FIR taps raw | `0x0C:0x07` | u16le | `08 00 -> 18 00` |

## Record Offsets

For Out1, output record base is absolute config offset `568`.

| Field | Absolute Offset | Record Offset |
| --- | ---: | ---: |
| FIR type | 596 | 28 |
| FIR window | 597 | 29 |
| FIR HighPass frequency | 598 | 30 |
| FIR LowPass frequency | 600 | 32 |
| FIR taps raw | 602 | 34 |
