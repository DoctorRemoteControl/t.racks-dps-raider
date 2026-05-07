# Gain Page InA/InD/Out1/Out8 GUI Capture Summary

Capture evidence date: 2026-05-07

Scripts:

- `script-example/303-auto-series-gain-page-ina-gain.dspd`
- `script-example/304-auto-capture-gain-page-ina-mute.dspd`
- `script-example/305-auto-capture-gain-page-ina-normal.dspd`
- `script-example/306-auto-series-gain-page-ind-gain.dspd`
- `script-example/307-auto-capture-gain-page-ind-mute.dspd`
- `script-example/308-auto-capture-gain-page-ind-normal.dspd`
- `script-example/309-auto-series-gain-page-out1-gain.dspd`
- `script-example/310-auto-capture-gain-page-out1-mute.dspd`
- `script-example/311-auto-capture-gain-page-out1-normal.dspd`
- `script-example/312-auto-series-gain-page-out8-gain.dspd`
- `script-example/313-auto-capture-gain-page-out8-mute.dspd`
- `script-example/314-auto-capture-gain-page-out8-normal.dspd`

Channel bytes:

```text
InA  = 0x00
InD  = 0x03
Out1 = 0x04
Out8 = 0x0B
```

## Gain Fader

Observed command:

```text
00 01 04 34 CC GG GG
```

Where:

- `CC` = channel byte
- `GG GG` = gain raw value, u16le at payload offsets 5..6

| Channel | Writes | Changing payload offsets | Last payload |
| --- | ---: | --- | --- |
| InA | 10 | `[5, 6]` | `00 01 04 34 00 00 00` |
| InD | 3 | `[5]` | `00 01 04 34 03 90 01` |
| Out1 | 7 | `[5, 6]` | `00 01 04 34 04 00 00` |
| Out8 | 3 | `[5]` | `00 01 04 34 0B 90 01` |

InD and Out8 changed only the low byte in these captures because the moved range
did not cross a high-byte boundary. InA and Out1 confirm the full u16le field.

## Mute Button

Observed command:

```text
00 01 03 35 CC SS
```

Where:

- `CC` = channel byte
- `SS` = mute state (`0x01` was captured when clicking Mute)

| Channel | Payload |
| --- | --- |
| InA | `00 01 03 35 00 01` |
| InD | `00 01 03 35 03 01` |
| Out1 | `00 01 03 35 04 01` |
| Out8 | `00 01 03 35 0B 01` |

## Normal Button

The visible `Normal` button on this page is not the mute-off command. It emits
the phase/polarity command:

```text
00 01 03 36 CC PP
```

Where:

- `CC` = channel byte
- `PP` = phase state byte

Fresh captures:

| Channel | Payload |
| --- | --- |
| InA | `00 01 03 36 00 00` |
| InD | `00 01 03 36 03 01` |
| Out1 | `00 01 03 36 04 00` |
| Out8 | `00 01 03 36 0B 01` |

The mixed `0x00` and `0x01` state bytes show that this button is a phase/polarity
state control. Existing safe pings identify `0x00` as the normal/0-degree state.
