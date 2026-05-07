# Matrix Out1/Out8 GUI Capture Summary

Capture evidence date: 2026-05-07

Existing stable GUI captures:

- `out/matrix/matrix-route-out1-ina/`
- `out/matrix/matrix-route-out1-inb/`
- `out/matrix/matrix-route-out1-inc/`
- `out/matrix/matrix-route-out8-ina/`
- `out/matrix/matrix-route-out8-inc/`
- `out/matrix/matrix-gain-out1-ina/`
- `out/matrix/matrix-gain-out1-inb/`
- `out/matrix/matrix-gain-out8-ind/`

Fresh retry scripts `285..289` were executed sequentially for Out1/Out8 route
and gain captures. All five captures produced command-specific writes.

Fresh capture outputs:

- `out/fader/matrix-route-out1-button-v2/`
- `out/fader/matrix-gain-out1-ina-series-v2/`
- `out/fader/matrix-route-out8-button-v2/`
- `out/fader/matrix-route-out8-ind-button/`
- `out/fader/matrix-gain-out8-ind-series-v2/`

## Route Button

Observed command:

```text
00 01 03 3A OO MM
```

Where:

- `OO` = output channel (`Out1=0x04` ... `Out8=0x0B`)
- `MM` = complete input route bitmask (`InA=0x01`, `InB=0x02`, `InC=0x04`, `InD=0x08`)

The route byte is the full current 4-bit route state for that output, not just
the clicked input. Button clicks toggle bits and write the resulting full mask.

Observed GUI route writes:

| Action | Payload |
| --- | --- |
| Out1 <- InA | `00 01 03 3A 04 01` |
| Out1 <- InB | `00 01 03 3A 04 02` |
| Out1 <- InC | `00 01 03 3A 04 04` |
| Out8 <- InA | `00 01 03 3A 0B 01` |
| Out8 <- InC | `00 01 03 3A 0B 04` |

Fresh sequential route captures:

| Action | Payload | Interpretation |
| --- | --- | --- |
| Click Out1 <- InB while InA was active | `00 01 03 3A 04 03` | Out1 mask `0x03` = InA+InB |
| Click Out8 <- InA while InD was active | `00 01 03 3A 0B 09` | Out8 mask `0x09` = InA+InD |
| Click Out8 <- InD while InA+InD was active | `00 01 03 3A 0B 01` | Out8 mask `0x01` = InA only |

## Matrix Gain Fader

Observed command:

```text
00 01 05 41 OO II GG GG
```

Where:

- `OO` = output channel (`Out1=0x04` ... `Out8=0x0B`)
- `II` = input index (`InA=0x00`, `InB=0x01`, `InC=0x02`, `InD=0x03`)
- `GG GG` = gain raw value, u16le

Observed GUI gain writes:

| Action | Payload |
| --- | --- |
| Out1 <- InA | `00 01 05 41 04 00 0E 01` |
| Out1 <- InB | `00 01 05 41 04 01 0E 01` |
| Out8 <- InD | `00 01 05 41 0B 03 0E 01` |

Fresh sequential fader captures:

| Action | Writes | Changing payload offsets | Last payload |
| --- | ---: | --- | --- |
| Out1 <- InA fader series | 280 | `[6, 7]` | `00 01 05 41 04 00 00 00` |
| Out8 <- InD fader series | 280 | `[6, 7]` | `00 01 05 41 0B 03 00 00` |

Known gain anchors:

```text
0.0 dB   raw 280 = 0x0118
-1.0 dB  raw 270 = 0x010E
-20.0 dB raw 80  = 0x0050
-30.0 dB raw 60  = 0x003C
-60.0 dB raw 0   = 0x0000
```
