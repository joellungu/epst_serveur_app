import 'package:get/get.dart';

class Requete extends GetConnect {
  //static var lien = 'https://educ-app-serveur-43d00822f87c.herokuapp.com/';
  //static var ws = 'educ-app-serveur-43d00822f87c.herokuapp.com';
  //
  static var lien = 'http://192.168.1.110:8080/';
  static var ws = '192.168.1.110:8080';
  //10.0.2.2
  // static var lien = 'http://192.168.1.71:8080/';
  // static var ws = '192.168.1.71:8080';

  Future<Response> getEs(String path) async {
    print(":: lien$path");
    return get(
      "$lien$path",
      headers: {
        "Accept": "*/*",
        "Content-Type": "application/json; charset=utf-8",
      },
    );
  }

  //
  Future<Response> postEs(String path, var data) async {
    return post(
      "$lien$path",
      data,
      headers: {
        "Accept": "*/*",
        "Content-Type": "application/json; charset=utf-8",
      },
    );
  }

  //
  Future<Response> putEs(String path, var data) async {
    return put(
      "$lien$path",
      data,
      headers: {
        "Accept": "*/*",
        "Content-Type": "application/json; charset=utf-8",
      },
    );
  }

  //
  Future<Response> deleteEs(String path) async {
    return delete(
      "$lien$path",
      headers: {
        "Accept": "*/*",
        "Content-Type": "application/json; charset=utf-8",
      },
    );
  }

  // Online learning API helpers
  Future<Response> createOnlineClass(Map<String, dynamic> payload) async {
    return postEs("online/classes", payload);
  }

  Future<Response> listOnlineClasses() async {
    return getEs("online/classes");
  }

  Future<Response> searchOnlineClasses({String? ecoleCode, String? province, String? district}) async {
    final params = <String>[];
    if (ecoleCode != null && ecoleCode.isNotEmpty) {
      params.add("ecoleCode=$ecoleCode");
    }
    if (province != null && province.isNotEmpty) {
      params.add("province=$province");
    }
    if (district != null && district.isNotEmpty) {
      params.add("district=$district");
    }
    final query = params.isEmpty ? "" : "?${params.join("&")}";
    return getEs("online/classes/search$query");
  }

  Future<Response> startLiveSession(Map<String, dynamic> payload) async {
    return postEs("online/sessions/start", payload);
  }

  Future<Response> joinLiveSession(Map<String, dynamic> payload) async {
    return postEs("online/sessions/join", payload);
  }

  Future<Response> leaveLiveSession(Map<String, dynamic> payload) async {
    return postEs("online/sessions/leave", payload);
  }

  Future<Response> endLiveSession(Map<String, dynamic> payload) async {
    return postEs("online/sessions/end", payload);
  }

  Future<Response> listSessionsByClass(String classIdOrLabel) async {
    final encoded = Uri.encodeComponent(classIdOrLabel);
    return getEs("online/sessions/class/$encoded");
  }

  Future<Response> listSessionParticipants(int sessionId) async {
    return getEs("online/sessions/$sessionId/participants");
  }

  Future<Response> createInspectionReport(Map<String, dynamic> payload) async {
    return postEs("online/inspections", payload);
  }

  Future<Response> listSchedulesByClass(
    String classIdOrLabel,
    String audience,
  ) async {
    final encoded = Uri.encodeComponent(classIdOrLabel);
    final query = Uri.encodeComponent(audience);
    return getEs("online/schedules/class/$encoded?audience=$query");
  }

  //
}
