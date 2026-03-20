import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:inspecteur_cours_distant/utils/requete.dart';

class InspecteurPage extends StatefulWidget {
  Map data;
  InspecteurPage(this.data, {Key? key}) : super(key: key);

  @override
  State<InspecteurPage> createState() => _InspecteurPageState();
}

class _InspecteurPageState extends State<InspecteurPage> {
  bool isLoading = true;
  String inspecteurNom = '';
  List<String> coursNoms = [];
  List<String> classeNoms = [];

  @override
  void initState() {
    super.initState();
    loadData();
  }

  Future<void> loadData() async {
    try {
      // 1️⃣ Récupération de la donnée principale
      // final response = await http.get(
      //   Uri.parse('https://exemple.com/api/inspecteur_data/1'),
      // );
      // final Map<String, dynamic> data = json.decode(response.body);

      final int inspecteurId = widget.data['idInspecteur'];
      final List<dynamic> coursIds = widget.data['cours'];
      final List<dynamic> classeIds = widget.data['classe'];

      // // 2️⃣ Récupération du nom de l'inspecteur
      // final inspecteurResponse = await http.get(
      //   Uri.parse('https://exemple.com/api/inspecteur/$inspecteurId'),
      // );
      // final inspecteurData = json.decode(inspecteurResponse.body);
      // inspecteurNom = inspecteurData['nom'];

      // 3️⃣ Récupération des noms des cours
      coursNoms = await Future.wait(
        coursIds.map((id) async {
          print("Cours 1: $id");
          final res = await http.get(
            Uri.parse('${Requete.lien}cours/one?id=$id'),
          );
          print("Cours 2: ${res.statusCode}");
          print("Cours 2: ${res.body}");
          final d = res.body.isNotEmpty ? json.decode(res.body) : {"cours": ""};
          return d['cours'];
        }),
      );

      // 4️⃣ Récupération des noms des classes
      classeNoms = await Future.wait(
        classeIds.map((id) async {
          print("Classe 1: $id");
          final res = await http.get(Uri.parse('${Requete.lien}classe/$id'));
          print("Classe 2: ${res.body}");
          final d = json.decode(res.body);
          return d['nom'];
        }),
      );

      // 5️⃣ Mise à jour de l’état
      setState(() {
        isLoading = false;
      });
    } catch (e) {
      print('Erreur : $e');
      setState(() => isLoading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    if (isLoading) {
      return const Scaffold(body: Center(child: CircularProgressIndicator()));
    }

    return Scaffold(
      //appBar: AppBar(title: const Text('Détails Inspecteur')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: ListView(
          children: [
            Text(
              'Inspecteur : $inspecteurNom',
              style: Theme.of(context).textTheme.titleLarge,
            ),
            const SizedBox(height: 20),
            Text('Cours', style: Theme.of(context).textTheme.titleMedium),
            ...coursNoms.map(
              (nom) =>
                  ListTile(leading: const Icon(Icons.book), title: Text(nom)),
            ),
            const Divider(),
            Text('Classes', style: Theme.of(context).textTheme.titleMedium),
            ...classeNoms.map(
              (nom) =>
                  ListTile(leading: const Icon(Icons.school), title: Text(nom)),
            ),
          ],
        ),
      ),
    );
  }
}
