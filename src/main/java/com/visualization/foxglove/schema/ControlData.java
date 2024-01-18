package com.visualization.foxglove.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ControlData {


    @JsonProperty("底盘号")
    private String chassisCode;

    @JsonProperty("终端id")
    private String terminalid;

    @JsonProperty("时间")
    private String gpstime;

    @JsonProperty("纬度")
    private String latitude;

    @JsonProperty("经度")
    private String longitude;

    @JsonProperty("海拔")
    private String height;

    @JsonProperty("SysstADMdmp")
    private String sysstADMdmp;

    @JsonProperty("位置跟踪误差")
    private String spdPlnlTrgLngErrmp;

    @JsonProperty("期望车速m/s")
    private String spdPlnvTrgSpdmp;

    @JsonProperty("期望加速度m/s2")
    private String spdPlnaTrgAccmp;

    @JsonProperty("行为ID")
    private String bhvCrdnnumBhvIDmp;

    @JsonProperty("坡度%")
    private String vehDarSlopmp;

    @JsonProperty("整车总质量kg")
    private String vehDamWghtmp;

    @JsonProperty("本车车速(km/h)")
    private String vehDavegoSpdmp;

    @JsonProperty("本车加速度m/s2")
    private String vehDaaEgoAccmp;

    @JsonProperty("变速箱档位")
    private String vehDastTraCurGearmp;

    @JsonProperty("变速箱传动比")
    private String vehDarTraCurGearmp;

    @JsonProperty("离合器开关")
    private String vehDastCluSwtmp;

    @JsonProperty("发动机摩擦扭矩百分比")
    private String vehDaprcTrqEngNomFricmp;

    @JsonProperty("制动设备源地址")
    private String vehDastSrcBrkmp;

    @JsonProperty("发动机实际扭矩百分比")
    private String vehDaprcActuTrqmp;

    @JsonProperty("驾驶员需求扭矩百分比")
    private String vehDaprcDrvrDmdTrqmp;

    @JsonProperty("发动机控制设备源地址")
    private String vehDastSrcEngCtrlmp;

    @JsonProperty("左前轮制动压力")
    private String vehDapFrontLeftmp;

    @JsonProperty("发动机转速")
    private String vehDanEngSpdmp;

    @JsonProperty("挂车状态")
    private String vehDastTrlrCnctnmp;

    @JsonProperty("传动系统结合状态")
    private String vehDastTraEgdmp;

    @JsonProperty("变速箱选择的档位")
    private String vehDastTraSelGearmp;

    @JsonProperty("变速箱换挡状态")
    private String vehDastTraShtmp;

    @JsonProperty("制动踏板开度(百分比)")
    private String vehDarBrkPedlmp;

    @JsonProperty("发动机预估损失扭矩百分比")
    private String vehDaprcTrqEstimdLossmp;

    @JsonProperty("变速箱TSC1限扭状态")
    private String vehDastTraTrqLimmp;

    @JsonProperty("真实油门开度百分比")
    private String vehDarAccrPedlmp;

    @JsonProperty("巡航设定车速")
    private String ACCSvSetFrmSysmp;

    @JsonProperty("变速箱输出轴转速")
    private String vehDanOutpShaftmp;

    @JsonProperty("制动系统准备可以释放")
    private String vehDastBrkReady4Rlsmp;

    @JsonProperty("角速度(rad/s)")
    private String yawrate;

    @JsonProperty("AX(m/s^2)")
    private String ax;

    @JsonProperty("AY(m/s^2)")
    private String ay;

    @JsonProperty("总驱动力")
    private String vDCfrDrvForce;
}
