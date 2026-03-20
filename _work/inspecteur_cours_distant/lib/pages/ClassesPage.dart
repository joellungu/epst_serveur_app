import 'dart:convert';
import 'dart:math';
import 'dart:ui';
import 'package:flutter/material.dart';
import 'package:flutter_svg/svg.dart';
import 'package:get/get.dart';
import 'package:http/http.dart' as http;
import 'package:inspecteur_cours_distant/pages/ContenuUploaderPage.dart';
import 'package:inspecteur_cours_distant/pages/cours/horaires_cours.dart';
import 'package:inspecteur_cours_distant/pages/cours/sessions_en_cours.dart';
import 'package:inspecteur_cours_distant/pages/direct.dart';
import 'package:inspecteur_cours_distant/utils/requete.dart';
import 'package:video_call_mobile/video_call.dart';

import 'direct.dart';

class ClassesPage extends StatefulWidget {
  Map data;
  String nom;
  ClassesPage(this.data, this.nom, {Key? key}) : super(key: key);

  @override
  State<ClassesPage> createState() => _ClassesPageState();
}

class _ClassesPageState extends State<ClassesPage> {
  bool isLoading = true;
  List<dynamic> classes = [];
  Map<int, List<dynamic>> coursParClasse = {}; // cache des cours déjà chargés

  final Random _random = Random();
  //
  int cls = 0;
  String categorie = "";

  @override
  void initState() {
    super.initState();
    fetchClasses();
  }

  Future<void> fetchClasses() async {
    //

    final List<dynamic> classeIds = widget.data['classe'];
    //
    classes = await Future.wait(
      classeIds.map((id) async {
        print("Classe 1: $id");
        final res = await http.get(Uri.parse('${Requete.lien}classe/$id'));
        print("Classe 2: ${res.body}");
        final d = json.decode(res.body);
        return d;
      }),
    );
    setState(() {
      isLoading = false;
      print("classes: $classes");
    });
  }

  Future<void> fetchCoursForClasse(int idClasse) async {
    if (coursParClasse.containsKey(idClasse)) return; // déjà chargé

    try {
      final response = await http.get(
        Uri.parse('${Requete.lien}cours/one?id=$idClasse'),
      );

      if (response.statusCode == 200) {
        setState(() {
          coursParClasse[idClasse] = json.decode(response.body);
        });
      } else {
        throw Exception('Erreur serveur');
      }
    } catch (e) {
      print('Erreur chargement cours : $e');
    }
  }

  void _openSchedules() {
    if (classes.isEmpty) {
      Get.snackbar("Oups", "Aucune classe disponible");
      return;
    }
    Navigator.of(context).push(
      MaterialPageRoute(
        builder: (c) => HorairesCoursInspecteur(classes: classes),
      ),
    );
  }

  void _openSessions() {
    if (classes.isEmpty) {
      Get.snackbar("Oups", "Aucune classe disponible");
      return;
    }
    Navigator.of(context).push(
      MaterialPageRoute(
        builder: (c) => SessionsEnCoursPage(classes: classes),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    if (isLoading) {
      return const Scaffold(body: Center(child: CircularProgressIndicator()));
    }

    return Scaffold(
      appBar: AppBar(
        title: const Text('Classes et Cours'),
        actions: [
          IconButton(
            icon: const Icon(Icons.calendar_month),
            tooltip: 'Horaires de cours',
            onPressed: _openSchedules,
          ),
          IconButton(
            icon: const Icon(Icons.live_tv),
            tooltip: 'Sessions en cours',
            onPressed: _openSessions,
          ),
        ],
      ),
      body: Column(
        children: [
          Card(
            //
            margin: EdgeInsets.all(15),
            color: Color.fromRGBO(
              _random.nextInt(128) + 127, // Valeur rouge entre 127 et 255
              _random.nextInt(128) + 127, // Valeur verte entre 127 et 255
              _random.nextInt(128) + 127, // Valeur bleue entre 127 et 255
              1,
            ),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(10),
            ),
            child: InkWell(
              onTap: () {
                //
                if (cls == 0) {
                  Get.snackbar("Oups", "Veuillez selectionner une classe");
                } else {
                  Navigator.of(context).push(
                    MaterialPageRoute(
                      builder: (c) {
                        //return Direct(widget.nom, "$cls $categorie");
                        return VideoCall(widget.nom);
                      },
                    ),
                  );
                  //Get.to();
                }
              },
              child: Container(
                height: 150,
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(10),
                ),
                width: double.maxFinite,
                child: Stack(
                  fit: StackFit.expand,
                  children: [
                    Container(
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(10),
                        // image: DecorationImage(
                        //   image: AssetImage(
                        //     "assets/arrire_plan.jpg",
                        //   ), // Image de fond
                        //   fit: BoxFit.cover, // Cover pour remplir le container
                        // ),
                      ),
                    ),
                    BackdropFilter(
                      filter: ImageFilter.blur(
                        sigmaX: 2,
                        sigmaY: 2,
                      ), // Effet de flou
                      child: Container(
                        color: Colors.black.withOpacity(
                          0.2,
                        ), // Opacité pour effet sombre
                      ),
                    ),
                    Center(child: Icon(Icons.play_circle)),
                    Align(
                      alignment: Alignment(0, 1),
                      child: Text(
                        "LIVE Professeur",
                        style: TextStyle(
                          color: Colors.black,
                          fontSize: 20,
                          fontWeight: FontWeight.w900,
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ),
          Expanded(
            flex: 9,
            child: ListView.builder(
              itemCount: classes.length,
              itemBuilder: (context, index) {
                final classe = classes[index];
                final idClasse = classe['id'];
                final nomClasse = classe['nom'];
                cls = classe['cls'];
                categorie = classe['categorie'];

                final cours = coursParClasse[idClasse];

                return ExpansionTile(
                  title: Text(
                    nomClasse,
                    style: const TextStyle(fontWeight: FontWeight.bold),
                  ),
                  leading: const Icon(Icons.school, color: Colors.blueAccent),
                  onExpansionChanged: (expanded) {
                    if (expanded) {
                      fetchCoursForClasse(idClasse);
                    }
                  },
                  children: [
                    if (cours == null)
                      const Padding(
                        padding: EdgeInsets.all(8.0),
                        child: CircularProgressIndicator(),
                      )
                    else if (cours.isEmpty)
                      const ListTile(title: Text('Aucun cours disponible'))
                    else
                      ...cours.map((c) {
                        return ListTile(
                          onTap: () {
                            //
                            Get.to(
                              ContenuUploaderPage(
                                idCours: c['idCours'],
                                idClasse: idClasse,
                                idInspecteur: widget.data['idInspecteur'],
                              ),
                            );
                          },
                          leading: const Icon(Icons.book, color: Colors.green),
                          title: Text(c['cours']),
                          subtitle: Text('Nom : ${c['cours']}'),
                        );
                      }).toList(),
                  ],
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}
