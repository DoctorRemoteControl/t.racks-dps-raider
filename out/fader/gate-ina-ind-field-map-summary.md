# Gate InA/InD GUI Capture Summary

Capture evidence date: 2026-05-07

Scripts:

- `script-example/294-auto-series-ina-gate-threshold.dspd`
- `script-example/295-auto-series-ina-gate-attack.dspd`
- `script-example/296-auto-series-ina-gate-hold.dspd`
- `script-example/297-auto-series-ina-gate-release.dspd`
- `script-example/298-auto-series-ind-gate-threshold.dspd`
- `script-example/299-auto-series-ind-gate-attack.dspd`
- `script-example/300-auto-series-ind-gate-hold.dspd`
- `script-example/301-auto-series-ind-gate-release.dspd`

Observed command:

```text
00 01 0A 3E CC AA AA RR RR HH HH TT TT
```

Where:

- `CC` = input channel (`InA=0x00`, `InB=0x01`, `InC=0x02`, `InD=0x03`)
- `AA AA` = attack raw, u16le at payload offsets 5..6
- `RR RR` = release raw, u16le at payload offsets 7..8
- `HH HH` = hold raw, u16le at payload offsets 9..10
- `TT TT` = threshold raw, u16le at payload offsets 11..12

## Fresh Captures

| Channel | Control | Writes | Changing payload offsets | Last payload |
| --- | --- | ---: | --- | --- |
| InA | Threshold | 12 | `[11]` | `00 01 0A 3E 00 E6 03 B7 0B E6 03 00 00` |
| InA | Attack | 9 | `[5, 6]` | `00 01 0A 3E 00 00 00 B7 0B E6 03 00 00` |
| InA | Hold | 15 | `[9, 10]` | `00 01 0A 3E 00 00 00 B7 0B 09 00 00 00` |
| InA | Release | 6 | `[7, 8]` | `00 01 0A 3E 00 00 00 09 00 09 00 00 00` |
| InD | Threshold | 8 | `[11]` | `00 01 0A 3E 03 00 00 09 00 09 00 B4 00` |
| InD | Attack | 7 | `[5, 6]` | `00 01 0A 3E 03 E6 03 09 00 09 00 B4 00` |
| InD | Hold | 6 | `[9, 10]` | `00 01 0A 3E 03 E6 03 09 00 E6 03 B4 00` |
| InD | Release | 15 | `[7, 8]` | `00 01 0A 3E 03 E6 03 B7 0B E6 03 B4 00` |

Threshold only changed payload offset 11 in these captures because the GUI range
is `-90.0 dB .. +0.0 dB`, raw `0..180`, so the high byte at offset 12 stayed
`0x00`.

## GUI Endpoint Anchors

From the supplied screenshot:

- InA all-max: Threshold `+0.0 dB`, Attack `999 ms`, Hold `999 ms`, Release `3000 ms`
- InD all-min: Threshold `-90.0 dB`, Attack `1 ms`, Hold `10 ms`, Release `10 ms`

Value anchors:

```text
threshold raw 0   = -90.0 dB
threshold raw 180 = +0.0 dB
attack raw 0      = 1 ms
attack raw 998    = 999 ms
hold raw 9        = 10 ms
hold raw 998      = 999 ms
release raw 9     = 10 ms
release raw 2999  = 3000 ms
```
