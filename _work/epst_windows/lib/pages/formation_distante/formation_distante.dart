import 'package:epst_windows_app/pages/formation_distante/InspecteurCoursScreen.dart';
import 'package:epst_windows_app/pages/formation_distante/details_inspecteur.dart';
import 'package:epst_windows_app/pages/formation_distante/horaires_admin.dart';
import 'package:epst_windows_app/utils/connexion.dart';
import 'package:epst_windows_app/utils/requette.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class FormationDistante extends StatefulWidget {
  final Map user;
  const FormationDistante(this.user, {Key? key}) : super(key: key);

  @override
  State<FormationDistante> createState() => _FormationDistanteState();
}

class _FormationDistanteState extends State<FormationDistante> {
  //
  final Rx<Widget> vue =
      Rx(const Center(child: Text("Selectionnez un inspecteur")));
  //
  final Requette requette = Requette();
  //
  int? _selectedInspecteurId;
  int? _selectedInspecteurRole;
  String _selectedInspecteurName = '';

  @override
  Widget build(BuildContext context) {
    //
    return Scaffold(
      body: Row(
        children: [
          Expanded(
            flex: 3,
            child: Column(
              children: [
                Container(
                  height: 50,
                ),
                FutureBuilder(
                    future: getAllInspecteurs(),
                    builder: (c, t) {
                      //
                      if (t.hasData) {
                        //
                        List list1 = t.data as List;
                        //
                        List liste = list1.where((e) {
                          int role = e['role'];
                          return role == 19 || role == 20;
                        }).toList();
                        //
                        return Expanded(
                          flex: 9,
                          child: ListView(
                            children: List.generate(liste.length, (a) {
                              //
                              Map ag = liste[a];
                              //
                              String roleLabel = _roleLabel(ag['role']);
                              //
                              return ListTile(
                                onTap: () {
                                  //
                                  print("Agent: $ag");
                                  _selectedInspecteurId = ag['id'];
                                  _selectedInspecteurRole = ag['role'];
                                  _selectedInspecteurName =
                                      "${ag['nom']} ${ag['postnom']} ${ag['prenom']}";
                                  //vue.value = DetailsInspecteur();
                                  //
                                  vue.value = InspecteurCoursScreen(
                                    ag['id'],
                                    roleInspecteur: ag['role'],
                                  );
                                },
                                leading: Icon(Icons.person),
                                title: Text(
                                    "${ag['nom']} ${ag['postnom']} ${ag['prenom']}"),
                                subtitle: Text(
                                    "$roleLabel / ${ag['numero']} / ${ag['province']}"),
                                trailing: Icon(Icons.arrow_forward_ios),
                              );
                            }),
                          ),
                        );
                      } else if (t.hasError) {
                        //
                        print("Le data: ${t.error}");
                        //
                        return Container();
                      }

                      return const Center(
                        child: SizedBox(
                          height: 30,
                          width: 30,
                          child: CircularProgressIndicator(),
                        ),
                      );
                    })
              ],
            ),
          ),
          Expanded(
            flex: 8,
            child: Column(
              children: [
                Container(
                  padding: const EdgeInsets.symmetric(
                    horizontal: 16,
                    vertical: 10,
                  ),
                  child: Row(
                    children: [
                      Expanded(
                        child: Text(
                          _selectedInspecteurName.isEmpty
                              ? "Aucun inspecteur selectionne"
                              : "Inspecteur: $_selectedInspecteurName",
                          style: const TextStyle(fontWeight: FontWeight.bold),
                        ),
                      ),
                      const SizedBox(width: 8),
                      ElevatedButton.icon(
                        onPressed: _selectedInspecteurId == null
                            ? null
                            : () {
                                vue.value = InspecteurCoursScreen(
                                  _selectedInspecteurId!,
                                  roleInspecteur: _selectedInspecteurRole ?? 19,
                                );
                              },
                        icon: const Icon(Icons.assignment_ind),
                        label: const Text("Affectation"),
                      ),
                      const SizedBox(width: 8),
                      OutlinedButton.icon(
                        onPressed: () {
                          vue.value = HorairesAdminScreen(widget.user);
                        },
                        icon: const Icon(Icons.calendar_month),
                        label: const Text("Horaires"),
                      ),
                    ],
                  ),
                ),
                const Divider(height: 1),
                Expanded(
                  child: Obx(() => vue.value),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  String _roleLabel(int role) {
    if (role == 19) return "Inspecteur sernafor (eleves)";
    if (role == 20) return "Inspecteur sernafor (enseignants)";
    return "Inspecteur";
  }

  //
  Future<List> getAllInspecteurs() async {
    //
    List<Map<String, dynamic>> liste = await Connexion.liste_utilisateur();
    //Response response = await requette.get("");
    //
    return liste;
  }
}
