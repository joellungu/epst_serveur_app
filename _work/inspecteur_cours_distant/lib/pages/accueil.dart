import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:inspecteur_cours_distant/pages/ClassesPage.dart';
import 'package:inspecteur_cours_distant/pages/accueil_classe.dart';
import 'package:inspecteur_cours_distant/pages/accueil_details.dart';
import 'package:inspecteur_cours_distant/utils/requete.dart';
import 'package:http/http.dart' as http;

class Accueil extends StatelessWidget {
  //
  int idInspecteur;
  String nom;
  //
  Accueil(this.idInspecteur, this.nom);
  //
  Requete requete = Requete();
  //
  @override
  Widget build(BuildContext context) {
    //
    return Scaffold(
      //appBar: AppBar(title: Text("")),
      body: FutureBuilder(
        future: getAllClasse(),
        builder: (c, t) {
          if (t.hasData) {
            List classe = t.data as List;
            //
            if (classe.isEmpty) {
              return Container();
            } else {
              return ClassesPage(classe[0], "Joel Lungu");
            }
            //
            //return InspecteurPage(classe[0]);
            //
          } else if (t.hasError) {
            print("Erreur: ${t.error}");
            return Container(color: Colors.grey);
          }
          return Center(
            child: Container(
              height: 30,
              width: 30,
              child: CircularProgressIndicator(),
            ),
          );
        },
      ),
    );
  }

  //
  Future<List> getAllClasse() async {
    //
    String baseUrl = '${Requete.lien}api/inspecteur-cours';

    final http.Client _client = http.Client();
    //Response response = await requete.get("");
    final params = {
      if (idInspecteur != null) 'idInspecteur': idInspecteur.toString(),
      'pageIndex': 0,
      'pageSize': 10,
      //if (sortBy != null) 'sortBy': sortBy,
      'sortAsc': true,
    };

    //final uri = Uri.parse(baseUrl).replace(queryParameters: params);
    final response = await _client.get(
      Uri.parse(
        "$baseUrl?pageIndex=0&pageSize=10&sortAsc=true&idInspecteur=$idInspecteur",
      ),
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
      },
    );

    _handleError(response);
    Map e = json.decode(response.body);

    List jsonResponse = e["data"];
    print("jsonResponse: $jsonResponse");
    //
    return jsonResponse;
  }

  void _handleError(http.Response response) {
    if (response.statusCode >= 400) {
      final error = json.decode(response.body);
      throw Exception(error['message'] ?? 'Erreur serveur');
    }
  }
}
