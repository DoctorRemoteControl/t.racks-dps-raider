# Out8 FIR Crossover GUI Capture Summary

Fresh scripts:

- `script-example/388-auto-capture-out8-fir-mode-toggle.dspd`
- `script-example/389-auto-series-out8-fir-highpass-frequency.dspd`
- `script-example/390-auto-series-out8-fir-lowpass-frequency.dspd`
- `script-example/391-auto-series-out8-fir-type.dspd`
- `script-example/392-auto-series-out8-fir-window.dspd`
- `script-example/393-auto-series-out8-fir-taps.dspd`
- `script-example/394-auto-capture-out8-fir-ok-apply.dspd`

As with Out1, the FIR generator page is commit-on-OK. Moving HighPass, LowPass, Type, Win, or Taps sends the complete `0x4B` generator payload only after OK.

## Mode

Out8 mode toggle produced two `0x4C` writes:

- `00 01 03 4C 0B 00` = Out8 IIR mode
- `00 01 03 4C 0B 01` = Out8 FIR mode

Only payload offset `5` changed.

## Generator Payload

All FIR generator OK writes use:

`00 01 0A 4B <channel> <type> <window> <hp_lo> <hp_hi> <lp_lo> <lp_hi> <taps_lo> <taps_hi>`

Fresh final Out8 restore/baseline payload:

`00 01 0A 4B 0B 00 06 6E 00 2C 01 08 00`

Decoded:

- channel `0x0B` = Out8
- type raw `0x00` = BYPASS
- window raw `0x06` = SINC
- highpass raw `0x006E` = 110
- lowpass raw `0x012C` = 300
- taps raw `0x0008` = 512 taps

## Field Captures

Type capture produced raw values `0x00`, `0x01`, `0x02`, `0x03`; only payload offset `5` changed.

Window capture changed only payload offset `6`. The fresh Out8 run observed raw values `0x00`, `0x01`, `0x02`, `0x05`, `0x06`, `0x07`, `0x08`, `0x09`, and `0x0A` with duplicates for some values. Out1 already provides the complete `0x00..0x0A` endpoint sequence.

Taps capture changed payload offset `11`; observed taps raw values include `0x0000`, `0x0008`, and `0x0018`.

Frequency examples:

- HighPass write: `00 01 0A 4B 0B 03 06 2B 01 2C 01 08 00`
- LowPass write: `00 01 0A 4B 0B 03 06 58 00 69 00 08 00`
