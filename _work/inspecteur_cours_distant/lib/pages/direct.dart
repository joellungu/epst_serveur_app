import 'dart:async';
import 'dart:math';
import 'package:flutter/material.dart';
import 'package:zego_uikit/zego_uikit.dart';
import 'package:zego_uikit_prebuilt_live_streaming/zego_uikit_prebuilt_live_streaming.dart'
    hide ZegoMenuBarButtonName, ZegoBottomMenuBarConfig;

class Direct extends StatefulWidget {
  final String roomId;
  final String userId;
  final String userName;
  final bool isHost;

  Direct({
    required this.roomId,
    required this.userId,
    required this.userName,
    required this.isHost,
  });

  @override
  State<StatefulWidget> createState() => _Direct();
}

class _Direct extends State<Direct> {
  @override
  void initState() {
    super.initState();
    Timer(const Duration(seconds: 1), () {
      ZegoUIKit().enableBeauty(true);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: ZegoUIKitPrebuiltLiveStreaming(
          appID: 1702990298,
          appSign: "546c30adae80723fbb7631cf10dc53ce086a0c5ff0f11629c057be48d5a6e619",
          userID: widget.userId.isNotEmpty ? widget.userId : _randomId(),
          userName: widget.userName,
          liveID: widget.roomId,
          config: widget.isHost
              ? (ZegoUIKitPrebuiltLiveStreamingConfig.host()
                ..layout = ZegoLayout.gallery(
                  showScreenSharingFullscreenModeToggleButtonRules:
                      ZegoShowFullscreenModeToggleButtonRules.alwaysShow,
                  showNewScreenSharingViewInFullscreenMode: true,
                )
                ..bottomMenuBar = ZegoLiveStreamingBottomMenuBarConfig(
                  hostButtons: [
                    ZegoLiveStreamingMenuBarButtonName.chatButton,
                    ZegoLiveStreamingMenuBarButtonName.switchCameraButton,
                    ZegoLiveStreamingMenuBarButtonName.toggleMicrophoneButton,
                    ZegoLiveStreamingMenuBarButtonName.toggleCameraButton,
                    ZegoLiveStreamingMenuBarButtonName.toggleScreenSharingButton,
                  ],
                ))
              : ZegoUIKitPrebuiltLiveStreamingConfig.audience(),
        ),
      ),
    );
  }

  String _randomId() {
    final random = Random();
    return List.generate(10, (_) => random.nextInt(10)).join();
  }
}
