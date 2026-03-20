import 'dart:convert';
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:file_picker/file_picker.dart';
import 'package:http/http.dart' as http;
import 'package:inspecteur_cours_distant/utils/requete.dart';

class ContenuUploaderPage extends StatefulWidget {
  final int idClasse;
  final int idCours;
  final int idInspecteur;

  const ContenuUploaderPage({
    super.key,
    required this.idClasse,
    required this.idCours,
    required this.idInspecteur,
  });

  @override
  State<ContenuUploaderPage> createState() => _ContenuUploaderPageState();
}

class _ContenuUploaderPageState extends State<ContenuUploaderPage> {
  String type = "NOTE"; // ou VIDEO, DEVOIR
  String titre = "";
  String description = "";
  File? fichier;

  Future<void> pickFile() async {
    final result = await FilePicker.platform.pickFiles();
    if (result != null) {
      setState(() {
        fichier = File(result.files.single.path!);
      });
    }
  }

  Future<void> uploadContenu() async {
    if (fichier == null) return;

    final uri = Uri.parse("${Requete.lien}contenus/upload");
    var request = http.MultipartRequest("POST", uri);
    Map fields = {};

    fields['titre'] = titre;
    fields['description'] = description;
    fields['type'] = type;
    fields['fichier'] = fichier!.readAsBytesSync();
    fields['idClasse'] = widget.idClasse.toString();
    fields['idCours'] = widget.idCours.toString();
    fields['idInspecteur'] = widget.idInspecteur.toString();
    fields['anneeScolaire'] = "2025-2026";
    fields['date'] = DateTime.now().toIso8601String();
    //
    print("Request: ${fields}");
    http.Response response = await http.post(
      uri,
      headers: {"Content-Type": "application/json"},
      body: jsonEncode(fields),
    );

    if (response.statusCode == 200 || response.statusCode == 201) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text("Contenu enregistré avec succès")),
      );
    } else {
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text("Erreur ${response.statusCode}")));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Uploader contenu")),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            DropdownButtonFormField<String>(
              value: type,
              items: const [
                DropdownMenuItem(value: "VIDEO", child: Text("Vidéo")),
                DropdownMenuItem(value: "NOTE", child: Text("Note du jour")),
                DropdownMenuItem(
                  value: "DEVOIR",
                  child: Text("Devoir du jour"),
                ),
              ],
              onChanged: (v) => setState(() => type = v!),
              decoration: const InputDecoration(labelText: "Type de contenu"),
            ),
            TextField(
              decoration: const InputDecoration(labelText: "Titre"),
              onChanged: (v) => titre = v,
            ),
            TextField(
              decoration: const InputDecoration(labelText: "Description"),
              onChanged: (v) => description = v,
            ),
            const SizedBox(height: 12),
            ElevatedButton.icon(
              onPressed: pickFile,
              icon: const Icon(Icons.attach_file),
              label: Text(
                fichier == null ? "Choisir un fichier" : "Fichier choisi ✅",
              ),
            ),
            const SizedBox(height: 20),
            ElevatedButton.icon(
              onPressed: uploadContenu,
              icon: const Icon(Icons.cloud_upload),
              label: const Text("Envoyer"),
            ),
          ],
        ),
      ),
    );
  }
}
