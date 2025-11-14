package fr.hequin0x.liveboxbypasscli.dto.response.mibs.gpon;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VEIP0(
        @JsonProperty("SerialNumber") String serialNumber,
        @JsonProperty("HardwareVersion") String hardwareVersion,
        @JsonProperty("VendorId") String vendorId,
        @JsonProperty("ONTSoftwareVersion0") String ontSoftwareVersion0,
        @JsonProperty("ONTSoftwareVersion1") String ontSoftwareVersion1
) { }
