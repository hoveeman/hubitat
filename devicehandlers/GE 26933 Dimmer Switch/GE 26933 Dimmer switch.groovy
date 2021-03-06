/**
 *  GE Motion Dimmer Switch (Model 26933) DTH
 *
 *  Copyright © 2018 Michael Struck
 *  Original Author: Matt Lebaugh (@mlebaugh)
 *
 *  Based off of the Dimmer Switch under Templates in the IDE 
 *
 *  Version 1.1.2 9/24/19 - fix refresh error   
 *  Version 1.1.1 12/4/18 - bug fixes, refresh now updates all settings, fix setlevel when not on
 *  Version 1.1.0 11/11/18 - Modified for Hubitat - jrf
 *
 *  Version 1.0.3 (8/21/18) - Changed the setLevel mode to boolean
 *  Version 1.0.2 (8/2/18) - Updated some of the text, added/updated options on the Settings page
 *  Version 1.0.1 (7/15/18) - Format and syntax updates. Thanks to @Darwin for the motion sensitivity/timeout minutes idea!
 *  Version 1.0.0 (3/17/17) Original release by Matt Lebaugh. Great Work!
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
	definition (name: "GE Motion Dimmer Switch 26933", namespace: "jrfarrar", author: "jrfarrar") {
	capability "Motion Sensor"
    capability "Actuator"
 	capability "Switch"
    capability "Switch Level"
	capability "Polling"
	capability "Refresh"
	capability "Sensor"
	capability "Light"
    capability "PushableButton"
        
		command "toggleMode"
        command "occupancy"
        command "occupied"
        command "vacancy"
        command "vacant"
        command "manual"
        command "setDefaultLevel"
        command "setMotionSenseLow"
        command "setMotionSenseMed"
        command "setMotionSenseHigh"
        command "setMotionSenseOff"
        command "lightSenseOn"
		command "lightSenseOff"
        command "setTimeout5Seconds"
        command "setTimeout1Minute"
        command "setTimeout5Minutes"
        command "setTimeout15Minutes"
        command "setTimeout30Minutes"
        command "switchModeOn"
        command "switchModeOff"
        
        attribute "operatingMode", "enum", ["Manual", "Vacancy", "Occupancy"]
        attribute "defaultLevel", "number"
        
        fingerprint mfr:"0063", prod:"494D", model: "3034", deviceJoinName: "GE Z-Wave Plus Motion Wall Dimmer"
	}
		preferences {
        	input title: "", description: "Select your preferences here, they will be sent to the device once updated.\n\nTo verify the current settings of the device, they will be shown in the 'Recently' page once any setting is updated", displayDuringSetup: false, type: "paragraph", element: "paragraph"
			//param 3
            input ("operationmode","enum",title: "Operating Mode",
                description: "Select the mode of the dimmer (no entry will keep the current mode)",
                options: [
                    "1" : "Manual (no auto-on/no auto-off)",
                    "2" : "Vacancy (no auto-on/auto-off)",
                    "3" : "Occupancy (auto-on/auto-off)"
                ],
                required: false
            )
            //param 1
            input ("timeoutduration","enum", title: "Timeout Duration (Occupancy/Vacancy)",
                description: "Time after no motion for light to turn off",
                options: [
                    "0" : "5 seconds",
                    "1" : "1 minute",
                    "5" : "5 minutes (Default)",
                    "15" : "15 minutes",
                    "30" : "30 minutes"
                ],
                required: false
            )
            input (name: "timeoutdurationPress", title: "Double Press Timeout (Occupancy/Vacancy)",
                description: "Physically press 'on' twice within 10 seconds to override timeout. Resets when light goes off",
                type: "enum",
                options: [
                    "0" : "5 seconds",
                    "1" : "1 minute",
                    "5" : "5 minutes",
                    "15" : "15 minutes",
                    "30" : "30 minutes"
                ],
                required: false
            )
            input (name: "modeOverride", title: "Double Press Operating Mode Override",
            	description: "Physically press 'off' twice within 10 seconds to override the current operating mode",
                type: "enum",
                options: [
                    "1" : "Manual (no auto-on/no auto-off)",
                    "2" : "Vacancy (no auto-on/auto-off)",
                    "3" : "Occupancy (auto-on/auto-off)"
                ],
                required: false
            )
            //param 6
            input "motion", "bool", title: "Enable Motion Sensor", defaultValue:true
            //param 13
			input ("motionsensitivity","enum", title: "Motion Sensitivity (When Motion Sensor Enabled)",
            	description: "Motion Sensitivity",
                options: [
                    "1" : "High",
                    "2" : "Medium (Default)",
                    "3" : "Low"
                ]
            )
            //param 14
            input "lightsense", "bool", title: "Enable Light Sensing (Occupancy)", defaultValue:true
            //param 5
            input "invertSwitch", "bool", title: "Invert Remote Switch Orientation", defaultValue:false
            //param 15
            input ("resetcycle","enum",title: "Motion Reset Cycle", description: "Time to stop reporting motion once motion has stopped",
                options: [
                    "0" : "Disabled",
                    "1" : "10 sec",
                    "2" : "20 sec (Default)",
                    "3" : "30 sec",
                    "4" : "45 sec",
                    "110" : "27 mins"
                ]
            )           
            //dimmersettings           
            //description
            input title: "", description: "**Z-Wave Dimmer Ramp Rate Settings**\nDefaults: Step: 1, Duration: 3", type: "paragraph", element: "paragraph"
            //param 9
            input "stepSize", "number", title: "Z-Wave Step Size (%)", range: "1..99", defaultValue: 1
       		//param 10
            input "stepDuration", "number", title: "Z-Wave Step Intervals Each 10 ms", range: "1..255", defaultValue: 3
            
            //description
            input title: "", description: "**Single Tap Ramp Rate Settings**", displayDuringSetup: false, type: "paragraph", element: "paragraph"
            //param 18
            input "dimrate", "bool", title: "Enable Slow Dim Rate (Off=Quick)", defaultValue:false
            //param 17
            input "switchlevel","number", title: "Default Dim Level", description:"Default 0: Return to last state, Max 99", range: "0..99", defaultValue: 0
            //descrip
            input title: "", description: "**Manual Ramp Rate Settings**\nFor push and hold\nDefaults: Step: 1, Duration: 3", type: "paragraph", element: "paragraph"
            //param 7
            input "manualStepSize", "number", title: "Manual Step Size (%)", range: "1..99", defaultValue: 1
       		//param 8
            input "manualStepDuration", "number", title: "Manual Step Intervals Each 10 ms", range: "1..255", defaultValue: 3
            //param 16
            input "switchmode", "bool", title: "Enable Switch Mode", defaultValue:false                             
            //association groups
        	input ( type: "paragraph", element: "paragraph",
            title: "", description: "**Configure Association Groups**\nDevices in association group 2 will receive Basic Set commands directly from the switch when it is turned on or off. Use this to control another device as if it was connected to this switch.\n\n" +
                         "Devices in association group 3 Same as Group 2 for this device\n\n" +
                         "Devices are entered as a comma delimited list of the Device Network IDs in hexadecimal format."
        	)			           
        	input ( name: "requestedGroup2", title: "Association Group 2 Members (Max of 5):", description: "Use the 'Device Network ID' for each device", type: "text", required: false )
        	input ( name: "requestedGroup3", title: "Association Group 3 Members (Max of 4):", description: "Use the 'Device Network ID' for each device", type: "text", required: false )            
            //description
            input title: "", description: "**setLevel Default Function (Advanced)**\nDefines how 'setLevel' behavior affects the light.",  type: "paragraph", element: "paragraph"
            input "setlevelmode", "bool", title: "setLevel Does Not Activate Light", defaultValue:false
            input name: "logEnable", type: "bool", title: "Enable debug logging", defaultValue: false
            input name: "txtEnable", type: "bool", title: "Enable descriptionText logging", defaultValue: true
    }
}

def parse(String description) {
    def result = null
	if (description != "updated") {
		//if (logEnable) log.debug "parse() >> zwave.parse($description)"
        def cmd = zwave.parse(description, [0x20: 1, 0x25: 1, 0x56: 1, 0x70: 2, 0x72: 2, 0x85: 2, 0x71: 3, 0x56: 1])
		if (cmd) {
			result = zwaveEvent(cmd)
			if (logEnable) log.debug("'$description' parsed to $result")
			if (txtEnable) log.info("${device.displayName} ${result}")
        }
	}
    if (!result) { log.warn "Parse returned ${result} for $description" }
    else {
		if (logEnable) log.debug "Parse returned ${result}"
	}
	
	return result
	
//	if (result?.name == 'hail' && hubFirmwareLessThan("000.011.00602")) {
//		result = [result, response(zwave.basicV1.basicGet())]
//		if (logEnable) log.debug "Was hailed: requesting state update"
//	}
//	return result
}

def zwaveEvent(hubitat.zwave.commands.crc16encapv1.Crc16Encap cmd) {
	if (logEnable) log.debug("zwaveEvent(): CRC-16 Encapsulation Command received: ${cmd}")
	def encapsulatedCommand = zwave.commandClass(cmd.commandClass)?.command(cmd.command)?.parse(cmd.data)
	if (!encapsulatedCommand) {
		if (logEnable) log.debug("zwaveEvent(): Could not extract command from ${cmd}")
	} else {
		return zwaveEvent(encapsulatedCommand)
	}
}
def zwaveEvent(hubitat.zwave.commands.basicv1.BasicReport cmd) {
	dimmerEvents(cmd)
}
def zwaveEvent(hubitat.zwave.commands.basicv1.BasicSet cmd) {
	def result = []
    if (cmd.value == 255) {
    	result << createEvent([name: "button", value: "pushed", data: [buttonNumber: "1"], descriptionText: "On/Up on (button 1) $device.displayName was pushed", isStateChange: true, type: "physical"])
    	if (timeoutdurationPress && state.Timer && (now()-state.Timer)<10000) {
        	if (logEnable) log.debug "Double press in less than 10 seconds-Overriding timeout"
        	def cmds=[]
            cmds << zwave.configurationV1.configurationSet(configurationValue: [timeoutdurationPress.toInteger()], parameterNumber: 1, size: 1)
       		cmds << zwave.configurationV1.configurationGet(parameterNumber: 1)
            sendHubCommand(cmds.collect{ new hubitat.device.HubAction(it.format()) }, 1000)
            showDashboard(timeoutdurationPress.toInteger(), "", "", "", "")
    	}
        state.Timer=now()
    }
	else if (cmd.value == 0) {
    	result << createEvent([name: "button", value: "pushed", data: [buttonNumber: "2"], descriptionText: "Off/Down (button 2) on $device.displayName was pushed", isStateChange: true, type: "physical"])
    	if (modeOverride && state.timerOff && (now()-state.timerOff)<10000) {
        	if (logEnable) log.debug "Double press in less than 10 seconds-Overriding mode"
        	def cmds=[]
            cmds << zwave.configurationV1.configurationSet(configurationValue: [modeOverride.toInteger()], parameterNumber: 3, size: 1)
       		cmds << zwave.configurationV1.configurationGet(parameterNumber: 3)
            sendHubCommand(cmds.collect{ new hubitat.device.HubAction(it.format()) }, 1000)
    	}
        state.timerOff=now()
    }
    return result
}
def zwaveEvent(hubitat.zwave.commands.associationv2.AssociationReport cmd) {
    if (logEnable) log.debug "---ASSOCIATION REPORT V2--- ${device.displayName} sent groupingIdentifier: ${cmd.groupingIdentifier} maxNodesSupported: ${cmd.maxNodesSupported} nodeId: ${cmd.nodeId} reportsToFollow: ${cmd.reportsToFollow}"
    state.group3 = "1,2"
    if (cmd.groupingIdentifier == 3) {
    	if (cmd.nodeId.contains(zwaveHubNodeId)) {
        	sendEvent(name: "numberOfButtons", value: 2, displayed: false)
        }
        else {
        	sendEvent(name: "numberOfButtons", value: 0, displayed: false)
			sendHubCommand(new hubitat.device.HubAction(zwave.associationV2.associationSet(groupingIdentifier: 3, nodeId: zwaveHubNodeId).format()))
			sendHubCommand(new hubitat.device.HubAction(zwave.associationV2.associationGet(groupingIdentifier: 3).format()))
        }
    }
}
def zwaveEvent(hubitat.zwave.commands.configurationv1.ConfigurationReport cmd) {
    if (logEnable) log.debug "---CONFIGURATION REPORT V1--- ${device.displayName} sent ${cmd}"
    def config = cmd.scaledConfigurationValue
    def result = []
    if (cmd.parameterNumber == 1) {
		def value = config == 0 ? "5 seconds" : config == 1 ? "1 minute" : config == 5 ? "5 minutes" : config == 15 ? "15 minutes" : "30 minutes"
    	result << createEvent([name:"TimeoutDuration", value: value, displayed:true, isStateChange:true])
    } else if (cmd.parameterNumber == 13) {
		def value = config == 1 ? "High" : config == 2 ? "Medium" : "Low"
    	result << createEvent([name:"MotionSensitivity", value: value, displayed:true, isStateChange:true])
	} else if (cmd.parameterNumber == 14) {
		def value = config == 1 ? "Enabled" : "Disabled"
    	result << createEvent([name:"LightSense", value: value, displayed:true, isStateChange:true])
    } else if (cmd.parameterNumber == 15) {
    	def value = config == 0 ? "Disabled" : config == 1 ? "10 sec" : config == 2 ? "20 sec" : config == 3 ? "30 sec" : config == 4 ? "45 sec" : "27 minute" 
    	result << createEvent([name:"ResetCycle", value: value, displayed:true, isStateChange:true])
    } else if (cmd.parameterNumber == 3) {
    	if (config == 1 ) {
        	result << createEvent([name:"operatingMode", value: "Manual", displayed:true, isStateChange:true])
         } else if (config == 2 ) {
        	result << createEvent([name:"operatingMode", value: "Vacancy", displayed:true, isStateChange:true])
        } else if (config == 3 ) {
        	result << createEvent([name:"operatingMode", value: "Occupancy", displayed:true, isStateChange:true])
        }
    } else if (cmd.parameterNumber == 6) {
    	def value = config == 1 ? "Enabled" : "Disabled"
    	result << createEvent([name:"MotionSensor", value: value, displayed:true, isStateChange:true])
    } else if (cmd.parameterNumber == 5) {
    	def value = config == 1 ? "Inverted" : "Normal"
    	result << createEvent([name:"SwitchOrientation", value: value, displayed:true, isStateChange:true])
    } 
    //dimmer settings
    	else if (cmd.parameterNumber == 7) {
    	result << createEvent([name:"StepSize", value: config, displayed:true, isStateChange:true])
    } else if (cmd.parameterNumber == 8) {
    	result << createEvent([name:"StepDuration", value: config, displayed:true, isStateChange:true])
    } else if (cmd.parameterNumber == 9) {
    	result << createEvent([name:"ManualStepSize", value: config, displayed:true, isStateChange:true])
    } else if (cmd.parameterNumber == 10) {
    	result << createEvent([name:"ManualStepDuration", value: config, displayed:true, isStateChange:true])
    } else if (cmd.parameterNumber == 16) {
    	result << createEvent([name:"SwitchMode", value: config, displayed:true, isStateChange:true])
    } else if (cmd.parameterNumber == 17) {
    	result << createEvent([name:"defaultLevel", value: config, displayed:true, isStateChange:true])
    } else if (cmd.parameterNumber == 18) {
    	result << createEvent([name:"DimRate", value: config, displayed:true, isStateChange:true])
    } else {
    log.warn "Parameter  ${cmd.parameterNumber} ${config}"}
    //
   return result
}
def zwaveEvent(hubitat.zwave.commands.switchbinaryv1.SwitchBinaryReport cmd) {
    def sstate = cmd.value ? "on" : "off"
    if (logEnable) log.debug "---BINARY SWITCH REPORT V1--- ${device.displayName} sent ${cmd}"
	//if (txtEnable) log.info "${device.displayName} switch is ${sstate} digital"   
    sendEvent(name: "switch", value: cmd.value ? "on" : "off", type: "digital")
}
def zwaveEvent(hubitat.zwave.commands.manufacturerspecificv2.ManufacturerSpecificReport cmd) {
    if (logEnable) log.debug "---MANUFACTURER SPECIFIC REPORT V2--- ${device.displayName} sent ${cmd}"
	if (logEnable) log.debug "manufacturerId:   ${cmd.manufacturerId}"
	if (logEnable) log.debug "manufacturerName: ${cmd.manufacturerName}"
    state.manufacturer=cmd.manufacturerName
	if (logEnable) log.debug "productId:        ${cmd.productId}"
	if (logEnable) log.debug "productTypeId:    ${cmd.productTypeId}"
	def msr = String.format("%04X-%04X-%04X", cmd.manufacturerId, cmd.productTypeId, cmd.productId)
	updateDataValue("MSR", msr)	
    sendEvent([descriptionText: "$device.displayName MSR: $msr", isStateChange: false])
}
def zwaveEvent(hubitat.zwave.commands.versionv1.VersionReport cmd) {
	def fw = "${cmd.applicationVersion}.${cmd.applicationSubVersion}"
	updateDataValue("fw", fw)
    if (logEnable) log.debug "---VERSION REPORT V1--- ${device.displayName} is running firmware version: $fw, Z-Wave version: ${cmd.zWaveProtocolVersion}.${cmd.zWaveProtocolSubVersion}"
}
def zwaveEvent(hubitat.zwave.commands.hailv1.Hail cmd) {
	createEvent([name: "hail", value: "hail", descriptionText: "Switch button was pressed", displayed: false])
}
def zwaveEvent(hubitat.zwave.commands.notificationv3.NotificationReport cmd){
    //if (logEnable) log.debug "---NOTIFICATION REPORT V3--- ${device.displayName} sent ${cmd}"
	def result = []
    def cmds = []
	if (cmd.notificationType == 0x07) {
		if ((cmd.event == 0x00)) { 
           	result << createEvent(name: "motion", value: "inactive", descriptionText: "$device.displayName motion has stopped")
            //if (txtEnable) log.info "${device.displayName} motion inactive"
         } else if (cmd.event == 0x08) {
            result << createEvent(name: "motion", value: "active", descriptionText: "$device.displayName detected motion")
            //if (txtEnable) log.info "${device.displayName} motion active"
        }
    }
	result  
}
def zwaveEvent(hubitat.zwave.Command cmd) {
    log.warn "${device.displayName} received unhandled command: ${cmd}"
}
def zwaveEvent(hubitat.zwave.commands.switchmultilevelv3.SwitchMultilevelReport cmd) {
    if (logEnable) log.debug "---SWITCH MULTILEVEL REPORT V3--- ${device.displayName} sent ${cmd}"
	dimmerEvents(cmd)
}
def zwaveEvent(hubitat.zwave.commands.switchmultilevelv3.SwitchMultilevelSet cmd) {
	if (logEnable) log.debug "---SWITCH MULTILEVEL SET V3--- ${device.displayName} sent ${cmd}"
	dimmerEvents(cmd)
}
def zwaveEvent(hubitat.zwave.commands.switchmultilevelv3.SwitchMultilevelStopLevelChange cmd) {
    if (logEnable) log.debug "---SWITCH MULTILEVEL Stop Level Change--- ${device.displayName} sent ${cmd}"
	dimmerEvents(cmd)
}
private dimmerEvents(hubitat.zwave.Command cmd) {
    def value = (cmd.value ? "on" : "off")
	def result = [createEvent(name: "switch", value: value)]
	if (cmd.value==0 && timeoutdurationPress){
    	if (logEnable) log.debug "Resetting timeout duration"
        def cmds=[],timeoutValue = timeoutduration ? timeoutduration.toInteger() : 5
        cmds << zwave.configurationV1.configurationSet(configurationValue: [timeoutValue], parameterNumber: 1, size: 1)
       	cmds << zwave.configurationV1.configurationGet(parameterNumber: 1)
        //sendHubCommand(cmds.collect{ new hubitat.device.HubAction(it.format()) }, 1000)
		delayBetween([cmds],500)
        //showDashboard(timeoutValue, "", "", "", "")
    }
    if (cmd.value && cmd.value <= 100) {
		//if (txtEnable) log.info "${device.displayName} level is: ${cmd.value}"
		result << createEvent(name: "level", value: cmd.value, unit: "%")
	}
	
	return result
}
def on() {
	//if (txtEnable) log.info "Digital On"
    delayBetween([zwave.basicV1.basicSet(value: 0xFF).format(), zwave.switchMultilevelV3.switchMultilevelGet().format()], 5000) 
}
def off() {
	//if (txtEnable) log.info "Digital Off"
	//if (txtEnable) log.info "${device.displayName} light is turning off"
	delayBetween ([zwave.basicV1.basicSet(value: 0x00).format(), zwave.switchMultilevelV3.switchMultilevelGet().format()], 5000)
}
def setLevel(value) {
	def valueaux = value as Integer
	def level = Math.min(valueaux, 99)
    def cmds = []    	
    if (settings.setlevelmode){ 
    	if (txtEnable) log.info "${device.displayName} setlevel does not activate light"
    	if (device.currentValue("switch") == "on") {
            if (txtEnable) log.info "${device.displayName} light already on setting level ${level}"
            sendEvent(name: "level", value: level, unit: "%")
			return delayBetween([
				secureCmd(zwave.configurationV1.configurationSet(scaledConfigurationValue: level , parameterNumber: 17, size: 1)),
				secureCmd(zwave.basicV1.basicSet(value: level)),
            	secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 17)),
				secureCmd(zwave.switchMultilevelV3.switchMultilevelGet())
			],500)
        } else if (device.currentValue("switch") == "off") {
            if (txtEnable) log.info "${device.displayName} light is off setting level ${level}"
			return delayBetween([
				secureCmd(zwave.configurationV1.configurationSet(scaledConfigurationValue: level , parameterNumber: 17, size: 1)),
            	secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 17))
			],500)
        }        
    } else {
        if (txtEnable) log.info "${device.displayName} setlevel activates light - level ${level}"
    	sendEvent(name: "level", value: level, unit: "%")
		delayBetween ([zwave.basicV1.basicSet(value: level).format(), zwave.switchMultilevelV3.switchMultilevelGet().format()], 500)
    }
}
def setLevel(value, duration) {
	def valueaux = value as Integer
	def level = Math.min(valueaux, 99)
	def dimmingDuration = duration < 128 ? duration : 128 + Math.round(duration / 60)
    sendEvent(name: "level", value: level, unit: "%")
	zwave.switchMultilevelV2.switchMultilevelSet(value: level, dimmingDuration: dimmingDuration).format()
}
def poll() { refresh() }
def ping() { refresh() }

def refresh() {
    if (logEnable) log.debug "refresh()"
	return delayBetween([
		 secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 1)),
		 secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 3)),
		 secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 5)),
		 secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 6)),
		 secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 7)),
		 secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 8)),
		 secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 9)),
		 secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 10)),
		 secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 13)),
		 secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 14)),
		 secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 15)),
		 secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 16)),
		 secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 17)),
		 secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 18)),
		 secureCmd(zwave.switchBinaryV1.switchBinaryGet()),
         secureCmd(zwave.switchMultilevelV1.switchMultilevelGet()),
		 //secureCmd(zwave.notificationV3.notificationGet(notificationType: 7)),
		 secureCmd(zwave.switchMultilevelV3.switchMultilevelGet())
    ],500)
}

//def refresh() {
//    log.info "refresh() is called"
//    delayBetween([
//    	zwave.notificationV3.notificationGet(notificationType: 7).format(),
//        zwave.switchMultilevelV3.switchMultilevelGet().format()
//	],100)
//    showVersion()
//    
//    //get params
//    //def cmds = []
//    //0.upto(20, {
//	//    cmds.add(secureCmd(zwave.configurationV1.configurationGet(parameterNumber: it)))	
//	//})
//    //return cmds
//}



def toggleMode() {
	if (logEnable) log.debug("Toggling Mode") 
    def cmds = []
    if (device.currentValue("operatingMode") == "Manual")  vacancy()
    else if (device.currentValue("operatingMode") == "Vacancy") occupancy()
    else if (device.currentValue("operatingMode") == "Occupancy") manual()
}
def setTimeout5Seconds() { setTimeoutMinutes(0) }
def setTimeout1Minute() { setTimeoutMinutes(1) }
def setTimeout5Minutes() { setTimeoutMinutes(5) }
def setTimeout15Minutes() { setTimeoutMinutes(15) }
def setTimeout30Minutes() { setTimeoutMinutes(30) }
def setTimeoutMinutes(value){
    def cmds = [], newTimeOut
    if (value==0) newTimeOut="5 seconds"
    else if (value==1) newTimeOut="1 minute"
    else if (value==5) newTimeOut="5 minutes"
    else if (value==15) newTimeOut="15 minutes"
    else if (value==30) newTimeOut="30 minutes"
	if (logEnable) log.debug "Setting timeout duration to: ${newTimeOut}"	
	return delayBetween([	
     	secureCmd(zwave.configurationV1.configurationSet(scaledConfigurationValue: value, parameterNumber: 1, size: 1)),
	 	secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 1))
	],500) 
}
def occupied() { occupancy() }
def occupancy() {
	if (logEnable) log.debug "Setting operating mode to: Occupancy"
	return delayBetween([
	    secureCmd(zwave.configurationV1.configurationSet(configurationValue: [3], parameterNumber: 3, size: 1)),
	    secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 3))
	],500)
}
def vacant() { vacancy() }
def vacancy() {
	if (logEnable) log.debug "Setting operating mode to: Vacancy"
	return delayBetween([
	    secureCmd(zwave.configurationV1.configurationSet(configurationValue: [2], parameterNumber: 3, size: 1)),
	    secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 3))
	],500)
}
def manual() {
	if (logEnable) log.debug "Setting operating mode to: Manual"
	return delayBetween([
	    secureCmd(zwave.configurationV1.configurationSet(configurationValue: [1], parameterNumber: 3, size: 1)),
	    secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 3))
	],500)
}
def setDefaultLevel(value) {
	if (logEnable) log.debug("Setting default level: ${value}%") 
    return delayBetween([
	    secureCmd(zwave.configurationV1.configurationSet(scaledConfigurationValue: value, parameterNumber: 17, size: 1)),
	    secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 17))
	],500) 
}
def setMotionSenseLow(){ setMotionSensitivity(3) }
def setMotionSenseMed(){ setMotionSensitivity(2) }
def setMotionSenseHigh(){ setMotionSensitivity(1) }
def setMotionSenseOff(){ setMotionSensitivity(0) }
def setMotionSensitivity(value) {
      def cmds = []
      if (value>0){
      	def mode=value==1 ? "High" : value==2 ? "Medium" : "Low"
      	if (logEnable) log.debug("Setting motion sensitivity to: ${mode}")
		  return delayBetween([
           secureCmd(zwave.configurationV1.configurationSet(configurationValue: [1], parameterNumber: 6, size: 1)),
	       secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 6)),
           secureCmd(zwave.configurationV1.configurationSet(configurationValue: [value], parameterNumber: 13, size: 1)),
	       secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 13))
		  ],500)
      }
      else if (value==0){
      	if (logEnable) log.debug("Turning off motion sensor")
		  return delayBetween([
           secureCmd(zwave.configurationV1.configurationSet(configurationValue: [0], parameterNumber: 6, size: 1)),
	       secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 6))
		  ],500)
     }
    return delayBetween([cmds],500)    
}
def lightSenseOn() {
	if (logEnable) log.debug("Setting Light Sense On") 
    return delayBetween([
	    secureCmd(zwave.configurationV1.configurationSet(configurationValue: [1], parameterNumber: 14, size: 1)),
	    secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 14))
	],500)
}
def lightSenseOff() {
	if (logEnable) log.debug("Setting Light Sense Off") 
    return delayBetween([
	    secureCmd(zwave.configurationV1.configurationSet(configurationValue: [0], parameterNumber: 14, size: 1)),
	    secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 14))
	],1000)   
}
def switchModeOn() {
	if (logEnable) log.debug ("Enabling Switch Mode")
    return delayBetween([
	  secureCmd(zwave.configurationV1.configurationSet(configurationValue: [1], parameterNumber: 16, size: 1)),
	  secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 16))
    ],500)
}
def switchModeOff(){
	if (logEnable) log.debug ("Disabling Switch Mode (Dimmer Mode)")
    return delayBetween([
      secureCmd(zwave.configurationV1.configurationSet(configurationValue: [0], parameterNumber: 16, size: 1)),
	  secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 16))
    ],500)
}
def installed() {
	updated()
}
def updated() {
	sendEvent(name: "checkInterval", value: 2 * 15 * 60 + 2 * 60, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID])
    if (!state.settingVar) state.settingVar=[]
    if (state.lastUpdated && now() <= state.lastUpdated + 3000) return
    state.lastUpdated = now()
	def cmds = [], timeDelay, motionSensor, lightSensor, dimLevel, switchMode
	//switch and dimmer settings
    //param 1 timeout duration
    if (settings.timeoutduration) {
       	timeDelay = timeoutduration.toInteger()
          cmds << secureCmd(zwave.configurationV1.configurationSet(configurationValue: [settings.timeoutduration.toInteger()], parameterNumber: 1, size: 1))
		  cmds << secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 1))
    }
    else{
        timeDelay = 5
          cmds << secureCmd(zwave.configurationV1.configurationSet(configurationValue: [5], parameterNumber: 1, size: 1))
	      cmds << secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 1)) 
    }
    //param 13 motion sensitivity
    if (settings.motionsensitivity) {
          cmds << secureCmd(zwave.configurationV1.configurationSet(configurationValue: [settings.motionsensitivity.toInteger()], parameterNumber: 13, size: 1))
	      cmds << secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 13)) 
        motionSensor=motionsensitivity.toInteger()
    }
    else {
          cmds << secureCmd(zwave.configurationV1.configurationSet(configurationValue: [2], parameterNumber: 13, size: 1))
	      cmds << secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 13)) 
        motionSensor=2
    }
    //param 14 lightsense
    lightSensor = lightsense ? 1 : 0
      cmds << secureCmd(zwave.configurationV1.configurationSet(configurationValue: [lightSensor], parameterNumber: 14, size: 1))
	  cmds << secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 14))    
    
	//param 15 reset cycle
	if (settings.resetcycle) {
          cmds << secureCmd(zwave.configurationV1.configurationSet(configurationValue: [settings.resetcycle.toInteger()], parameterNumber: 15, size: 1))
	      cmds << secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 15))   
	}
    else {
            cmds << secureCmd(zwave.configurationV1.configurationSet(configurationValue: [2], parameterNumber: 15, size: 1))
            cmds << secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 15))  
    }
    //param 3 operating mode (no default...no entry=no change)
    if (settings.operationmode) secureCmd(zwave.configurationV1.configurationSet(configurationValue: [settings.operationmode.toInteger()], parameterNumber: 3, size: 1))
	    secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 3))
    
    //param 6 motion
    motionSensor = !motion ? 0 : motionSensor
    mymotion = motion ? 1 : 0
        cmds << secureCmd(zwave.configurationV1.configurationSet(configurationValue: [mymotion.toInteger()], parameterNumber: 6, size: 1))
	    cmds << secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 6))  
    //param 5 invert switch
    myinvert = invertSwitch ? 1 : 0 
    //cmds << zwave.configurationV1.configurationSet(configurationValue: [invertSwitch ? 1 : 0 ], parameterNumber: 5, size: 1)
    //cmds << zwave.configurationV1.configurationGet(parameterNumber: 5)
    //return delayBetween([
        cmds << secureCmd(zwave.configurationV1.configurationSet(configurationValue: [1], parameterNumber: 5, size: 1))
	    cmds << secureCmd(zwave.configurationV1.configurationGet(parameterNumber: 5))  
    //],500)
    
    cmds << zwave.associationV1.associationSet(groupingIdentifier:0, nodeId:zwaveHubNodeId)
    cmds << zwave.associationV1.associationSet(groupingIdentifier:1, nodeId:zwaveHubNodeId)
	cmds << zwave.associationV1.associationSet(groupingIdentifier:2, nodeId:zwaveHubNodeId)
	cmds << zwave.associationV1.associationSet(groupingIdentifier:3, nodeId:zwaveHubNodeId)
        
	def nodes = []
	if (settings.requestedGroup2 != state.currentGroup2) {
        nodes = parseAssocGroupList(settings.requestedGroup2, 2)
       	cmds << zwave.associationV2.associationRemove(groupingIdentifier: 2, nodeId: [])
       	cmds << zwave.associationV2.associationSet(groupingIdentifier: 2, nodeId: nodes)
       	cmds << zwave.associationV2.associationGet(groupingIdentifier: 2)
       	state.currentGroup2 = settings.requestedGroup2
	}
    if (settings.requestedGroup3 != state.currentGroup3) {
       	nodes = parseAssocGroupList(settings.requestedGroup3, 3)
       	cmds << zwave.associationV2.associationRemove(groupingIdentifier: 3, nodeId: [])
       	cmds << zwave.associationV2.associationSet(groupingIdentifier: 3, nodeId: nodes)
       	cmds << zwave.associationV2.associationGet(groupingIdentifier: 3)
       	state.currentGroup3 = settings.requestedGroup3
    }
	// end switch and dimmer settings
		        
    // dimmer specific settings
    //param 7 zwave step
    if (settings.stepSize) {
       	cmds << zwave.configurationV1.configurationSet(configurationValue: [settings.stepSize.toInteger()], parameterNumber: 7, size: 1)    
       	cmds << zwave.configurationV1.configurationGet(parameterNumber: 7)
    }
    //param 8 zwave duration
    if (settings.stepDuration) {
       	cmds << zwave.configurationV1.configurationSet(configurationValue: [0,settings.stepDuration.toInteger()], parameterNumber: 8, size: 2)
       	cmds << zwave.configurationV1.configurationGet(parameterNumber: 8)
    }
    //param 9 manual step
    if (settings.manualStepSize) {
       	cmds << zwave.configurationV1.configurationSet(configurationValue: [settings.manualStepSize.toInteger()], parameterNumber: 9, size: 1)
       	cmds << zwave.configurationV1.configurationGet(parameterNumber: 9)
    }
    //param 10 manual duration
    if (settings.manualStepDuration) {
      	cmds << zwave.configurationV1.configurationSet(configurationValue: [0,settings.manualStepDuration.toInteger()], parameterNumber: 10, size: 2)
      	cmds << zwave.configurationV1.configurationGet(parameterNumber: 10)  
	}
    //switch mode param 16
    switchMode = switchmode ? 1 : 0
    cmds << zwave.configurationV1.configurationSet(configurationValue: [switchMode], parameterNumber: 16, size: 1)
    cmds << zwave.configurationV1.configurationGet(parameterNumber: 16)
    //switch level param 17
    if (settings.switchlevel == "0" || !switchlevel) {
       	cmds << zwave.configurationV1.configurationSet(configurationValue: [0], parameterNumber: 17, size: 1)
        dimLevel =0
    } 
    else if (settings.switchlevel) {
       	cmds << zwave.configurationV1.configurationSet(configurationValue: [settings.switchlevel.toInteger()], parameterNumber: 17, size: 1)
    	cmds << zwave.configurationV1.configurationGet(parameterNumber: 17)
        dimLevel =switchlevel.toInteger()
    }
    //dim rate param 18
	cmds << zwave.configurationV1.configurationSet(configurationValue: [dimrate ? 1 : 0], parameterNumber: 18, size: 1)
    cmds << zwave.configurationV1.configurationGet(parameterNumber: 18)

    //end of dimmer specific params   
    

    showDashboard(timeDelay, motionSensor, lightSensor, dimLevel, switchMode)
    showVersion()    
    //sendHubCommand(cmds.collect{ new hubitat.device.HubAction(it.format()) }, 500)
    //return cmds
    
    return delayBetween([cmds],500)
    

}
def configure() {
	def cmds = []
	// Make sure lifeline is associated
	cmds << zwave.associationV1.associationSet(groupingIdentifier:0, nodeId:zwaveHubNodeId)
    cmds << zwave.associationV1.associationSet(groupingIdentifier:1, nodeId:zwaveHubNodeId)
	cmds << zwave.associationV1.associationSet(groupingIdentifier:2, nodeId:zwaveHubNodeId)
	cmds << zwave.associationV1.associationSet(groupingIdentifier:3, nodeId:zwaveHubNodeId)
    //sendHubCommand(cmds.collect{ new hubitat.device.HubAction(it.format()) }, 1000)
    return delayBetween([cmds],500)
}
private parseAssocGroupList(list, group) {
    def nodes = group == 2 ? [] : [zwaveHubNodeId]
    if (list) {
        def nodeList = list.split(',')
        def max = group == 2 ? 5 : 4
        def count = 0

        nodeList.each { node ->
            node = node.trim()
            if ( count >= max) {
                log.warn "Association Group ${group}: Number of members is greater than ${max}! The following member was discarded: ${node}"
            }
            else if (node.matches("\\p{XDigit}+")) {
                def nodeId = Integer.parseInt(node,16)
                if (nodeId == zwaveHubNodeId) {
                	log.warn "Association Group ${group}: Adding the hub as an association is not allowed (it would break double-tap)."
                }
                else if ( (nodeId > 0) & (nodeId < 256) ) {
                    nodes << nodeId
                    count++
                }
                else {
                    log.warn "Association Group ${group}: Invalid member: ${node}"
                }
            }
            else {
                log.warn "Association Group ${group}: Invalid member: ${node}"
            }
        }
    }  
    return nodes
}
def showDashboard(timeDelay, motionSensor, lightSensor, dimLevel, switchMode) {
    if (timeDelay=="") timeDelay=state.currentTimeDelay
    if (motionSensor=="") motionSensor=state.currentMotionSensor
    if (lightSensor=="") lightSensor=state.currentLightSensor
    if (dimLevel=="") dimLevel=state.currentDimLevel
    if (switchMode=="") switchMode = state.currentSwitchMode
    state.currentTimeDelay=timeDelay
    state.currentMotionSensor=motionSensor
    state.currentLightSensor=lightSensor
    state.currentDimLevel = dimLevel
    state.currentSwitchMode = switchMode
    def motionSensorTxt = motionSensor == 1 ? "High" : motionSensor == 2 ? "Medium" : motionSensor==3 ? "Low" : "Disabled"
    def lightSensorTxt = lightSensor == 0 ? "Disabled" : "Enabled"
    def timeDelayTxt = timeDelay == 0 ? "5 seconds" : timeDelay == 1 ? "1 minute" : timeDelay == 5 ? "5 minutes" : timeDelay == 15 ? "15 minutes" : "30 minutes"
    def dimLevelTxt = dimLevel +"%"
    def switchModeTxt = switchMode ? "Enabled" : "Disabled"
    def dimSync = (switchlevel && state.currentDimLevel == switchlevel.toInteger()) || (!switchlevel && state.currentDimLevel== 0) ? "✔" : "‼"
    def motionSync = ((motion && motionSensorTxt !="Disabled" && motionsensitivity && state.currentMotionSensor==motionsensitivity.toInteger()) || (!motionsensitivity && state.currentMotionSensor==2)) ||
    	(!motion && motionSensorTxt =="Disabled")  ? "✔" : "‼"
    def lightSync = (!lightsense && state.currentLightSensor == 0) || (lightsense && state.currentLightSensor == 1) ? "✔" : "‼"
    def timeSync = (timeoutduration && state.currentTimeDelay == timeoutduration.toInteger()) || (!timeoutduration && state.currentTimeDelay == 5)  ? "✔" : "‼"
    def switchSync = (switchmode && state.currentSwitchMode == 1) || (!switchmode && state.currentSwitchMode == 0) ? "✔" : "‼"
    String result =""
   	result +="${dimSync} Default Dim Level: " + dimLevelTxt
    result +="\n${motionSync} Motion Sensitivity: " + motionSensorTxt
   	result +="\n${lightSync} Light Sensing: " + lightSensorTxt
	result +="\n${timeSync} Timeout Duration: " + timeDelayTxt
    result +="\n${switchSync} Switch Mode: " + switchModeTxt
	sendEvent (name:"dashboard", value: result ) 
}

private secureCmd(cmd) {
    if (getDataValue("zwaveSecurePairingComplete") == "true") {
	return zwave.securityV1.securityMessageEncapsulation().encapsulate(cmd).format()
    } else {
	return cmd.format()
    }	
}
private command(hubitat.zwave.Command cmd) {
    if (state.sec) {
        zwave.securityV1.securityMessageEncapsulation().encapsulate(cmd).format()
    } else {
        cmd.format()
    }
}
private commands(commands, delay=500) {
    delayBetween(commands.collect{ command(it) }, delay)
}

def showVersion() { sendEvent (name: "about", value:"DTH Version 1.1.2 (9/24/19)") }

