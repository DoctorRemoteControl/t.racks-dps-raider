# Out1 FIR Crossover GUI Capture Summary

Fresh scripts:

- `script-example/376-auto-capture-out1-fir-mode-toggle.dspd`
- `script-example/377-auto-series-out1-fir-highpass-frequency.dspd`
- `script-example/378-auto-series-out1-fir-lowpass-frequency.dspd`
- `script-example/379-auto-series-out1-fir-type.dspd`
- `script-example/380-auto-series-out1-fir-window.dspd`
- `script-example/381-auto-series-out1-fir-taps.dspd`
- `script-example/382-auto-capture-out1-fir-ok-apply.dspd`

Important behavior: the FIR generator page is commit-on-OK. Moving HighPass, LowPass, Type, Win, or Taps does not send the generator payload until OK is pressed. OK sends or duplicates the complete `0x4B` generator payload.

## Mode

Out1 mode toggle produced two `0x4C` writes:

- `00 01 03 4C 04 00` = Out1 IIR mode
- `00 01 03 4C 04 01` = Out1 FIR mode

Only payload offset `5` changed.

## Generator Payload

All FIR generator OK writes use:

`00 01 0A 4B <channel> <type> <window> <hp_lo> <hp_hi> <lp_lo> <lp_hi> <taps_lo> <taps_hi>`

Fresh final Out1 restore/baseline payload:

`00 01 0A 4B 04 03 0A 91 00 92 00 08 00`

Decoded:

- channel `0x04` = Out1
- type raw `0x03`
- window raw `0x0A`
- highpass raw `0x0091` = 145
- lowpass raw `0x0092` = 146
- taps raw `0x0008` = 512 taps

## Field Captures

Type capture produced raw values `0x00`, `0x01`, `0x02`, `0x03` and one duplicate `0x03`; only payload offset `5` changed.

Window capture produced raw values `0x00..0x0A`; only payload offset `6` changed. Exact label anchors already observed include HAMMING at raw `0x03` and SINC at raw `0x06`; raw `0x00`, `0x09`, and `0x0A` are newly observed valid values from the GUI dropdown.

Taps capture changed payload offset `11`; observed taps raw values include `0x0000`, `0x0008`, and `0x0018`.

HighPass and LowPass captures confirmed payload offsets `7/8` and `9/10` respectively. Fresh examples:

- HighPass write: `00 01 0A 4B 04 03 06 91 00 2C 01 08 00`
- LowPass write: `00 01 0A 4B 04 03 06 91 00 92 00 08 00`
