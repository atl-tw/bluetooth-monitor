Bluetooth Monitor
=================


This is a simple set of tools for monitoring Bluetooth Devices in an
area around a Raspberry Pi (or, really any Linux box).

There will be two main bits:


Watcher
-------

This is a process that needs to run as root so it can sniff the
bluetooth stuff. It works by kicking off a continuous "hcitool lescan"
to search for BLE devices, a continuous scan with "bluetoothctl",
and finally watches the output from btmon as the scans are firing to
to capture extended data. It will this generate MQTT messages with
the RSSI and Device Type information. It also keeps a cache of all the
devices it has seen in the last N minutes and will attempt a l2ping on
on the mac addresses every N minutes to determine when something falls
out of the area.

You run multiple Watchers in different areas to gather data.


Tracker
-------

In order to generate action positional data the bluetooth devices,
you need multiple Watchers running and capturing the RSSI information
for different areas. The Tracker then takes the MQTT events and derives
the location, and publishes another MQTT event to store the position
information for the device.

You just need to run a single Tracker.