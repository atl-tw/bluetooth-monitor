package com.thoughtworks.bluetooth.monitor;

/**
 * Created by rcooper on 4/20/16.
 */
public final class TestConstants {

    public static final String HCI_EVENT = "> HCI Event: LE Meta Event (0x3e) plen 27                                                                                             [hci0] 3065.779073\n" +
            "      LE Advertising Report (0x02)\n" +
            "        Num reports: 1\n" +
            "        Event type: Connectable undirected - ADV_IND (0x00)\n" +
            "        Address type: Public (0x00)\n" +
            "        Address: C8:69:CD:3E:20:F1 (OUI C8-69-CD)\n" +
            "        Data length: 15\n" +
            "        Flags: 0x1a\n" +
            "          LE General Discoverable Mode\n" +
            "          Simultaneous LE and BR/EDR (Controller)\n" +
            "          Simultaneous LE and BR/EDR (Host)\n" +
            "        Company: Apple, Inc. (76)\n" +
            "          Type: Apple TV (9)\n" +
            "          Data: 038f00000000\n" +
            "        RSSI: -105 dBm (0x97)";
    public static final String BT_MON_EVENT = "@ Device Found: C8:69:CD:83:BD:E7 (1) rssi -98 flags 0x0000";

    private TestConstants(){}
}
