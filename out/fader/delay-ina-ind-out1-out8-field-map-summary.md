# Delay GUI Fader Capture Map

Capture date: 2026-05-07

Scripts:

- `script-example/278-auto-series-ina-delay.dspd`
- `script-example/279-auto-series-ind-delay.dspd`
- `script-example/280-auto-series-out1-delay.dspd`
- `script-example/281-auto-series-out8-delay.dspd`

Observed command:

```text
0x38
```

Observed payload:

```text
00 01 04 38 CC DD DD
```

Where:

- `CC` = channel index
- `DD DD` = delay raw value, u16le

The delay fader GUI captures changed only payload offsets `5..6`.

| Channel | Channel byte | Series count | Changing payload offsets | Observed raw values |
| --- | ---: | ---: | --- | --- |
| InA | `0x00` | 28 | `[5, 6]` | `64830, 63029, 61678, 60778, 60328, 58077, 56276, 54925, 54475, 50423, 48622, 46822, 46371, 40068, 38268, 35566, 29714, 27463, 24311, 21160, 16207, 13056, 11255, 10355, 5853, 4052, 1801, 0` |
| InD | `0x03` | 20 | `[5, 6]` | `64830, 63929, 62129, 58077, 56726, 55375, 53124, 46822, 45021, 42770, 41419, 36017, 31965, 28813, 26562, 17108, 12606, 8554, 6753, 0` |
| Out1 | `0x04` | 29 | `[5, 6]` | `450, 900, 1351, 2701, 3602, 4502, 5402, 8104, 9454, 11255, 12606, 17558, 20259, 21610, 22510, 27913, 30164, 32415, 36467, 38718, 40519, 41419, 45021, 46822, 49073, 50423, 61228, 63479, 65280` |
| Out8 | `0x0B` | 27 | `[5, 6]` | `450, 1351, 3151, 6303, 7203, 8104, 11705, 13956, 15757, 16207, 20710, 22510, 24761, 27913, 31514, 35116, 36017, 38718, 41419, 45021, 46822, 50423, 52224, 54475, 56726, 63929, 65280` |

Endpoint anchors:

```text
0 raw     = 0.000 ms
65280 raw = 680.000 ms
ms = raw / 96.0
```

Conclusion:

The GUI captures anchor both input and output delay writes. The channel index is linear from `InA=0x00` through `Out8=0x0B`, and the delay value is stored as a single `u16le` at payload offset `5`.
