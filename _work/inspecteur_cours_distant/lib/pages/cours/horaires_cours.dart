import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:inspecteur_cours_distant/utils/requete.dart';

class HorairesCoursInspecteur extends StatefulWidget {
  final List<dynamic> classes;

  const HorairesCoursInspecteur({
    Key? key,
    required this.classes,
  }) : super(key: key);

  @override
  State<HorairesCoursInspecteur> createState() => _HorairesCoursInspecteurState();
}

class _HorairesCoursInspecteurState extends State<HorairesCoursInspecteur> {
  final Requete requete = Requete();
  String audience = 'STUDENT';
  final Map<String, Future<List<Map<String, dynamic>>>> _scheduleFutures = {};

  String _formatDate(String? value) {
    if (value == null || value.isEmpty) return '-';
    try {
      final dt = DateTime.parse(value);
      return DateFormat('dd/MM/yyyy HH:mm').format(dt);
    } catch (_) {
      return value;
    }
  }

  String _classTitle(Map<String, dynamic> classe) {
    final parts = [
      classe['niveau'],
      classe['cycle'],
      classe['section'],
      classe['option'],
      classe['nom'],
    ];
    return parts
        .where((e) => e != null && e.toString().trim().isNotEmpty)
        .map((e) => e.toString())
        .join(' ');
  }

  Future<List<Map<String, dynamic>>> _loadSchedules(String classId) async {
    final response = await requete.listSchedulesByClass(classId, audience);
    if (response.statusCode == 200 || response.statusCode == 201) {
      final body = response.body;
      if (body is List) {
        return body.map((e) => Map<String, dynamic>.from(e)).toList();
      }
    }
    return [];
  }

  Future<List<Map<String, dynamic>>> _getSchedules(String classId) {
    return _scheduleFutures.putIfAbsent(classId, () => _loadSchedules(classId));
  }

  void _onAudienceChange(String value) {
    setState(() {
      audience = value;
      _scheduleFutures.clear();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Horaires de cours')),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(16),
            child: Row(
              children: [
                const Text('Public: '),
                const SizedBox(width: 8),
                DropdownButton<String>(
                  value: audience,
                  items: const [
                    DropdownMenuItem(
                      value: 'STUDENT',
                      child: Text('Elèves'),
                    ),
                    DropdownMenuItem(
                      value: 'TEACHER',
                      child: Text('Enseignants'),
                    ),
                  ],
                  onChanged: (value) {
                    if (value != null) {
                      _onAudienceChange(value);
                    }
                  },
                ),
              ],
            ),
          ),
          Expanded(
            child: ListView.builder(
              padding: const EdgeInsets.all(16),
              itemCount: widget.classes.length,
              itemBuilder: (context, index) {
                final classe = Map<String, dynamic>.from(widget.classes[index]);
                final classId = (classe['id'] ?? '').toString();
                final title = _classTitle(classe);
                return Card(
                  margin: const EdgeInsets.only(bottom: 12),
                  child: ExpansionTile(
                    title: Text(title.isEmpty ? 'Classe' : title),
                    leading: const Icon(Icons.school),
                    children: [
                      FutureBuilder<List<Map<String, dynamic>>>(
                        future: _getSchedules(classId),
                        builder: (context, snapshot) {
                          if (snapshot.connectionState == ConnectionState.waiting) {
                            return const Padding(
                              padding: EdgeInsets.all(16),
                              child: CircularProgressIndicator(),
                            );
                          }
                          final schedules = snapshot.data ?? [];
                          if (schedules.isEmpty) {
                            return const Padding(
                              padding: EdgeInsets.all(16),
                              child: Text('Aucun horaire disponible'),
                            );
                          }
                          return Column(
                            children: schedules.map((item) {
                              return ListTile(
                                title: Text(item['title'] ?? 'Cours'),
                                subtitle: Text(
                                  '${_formatDate(item['startsAt'])} - ${_formatDate(item['endsAt'])}',
                                ),
                                trailing: Text(item['audience'] ?? ''),
                              );
                            }).toList(),
                          );
                        },
                      ),
                    ],
                  ),
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}
