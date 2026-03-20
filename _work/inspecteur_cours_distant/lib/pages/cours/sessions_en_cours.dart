import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:inspecteur_cours_distant/utils/requete.dart';

class SessionsEnCoursPage extends StatefulWidget {
  final List<dynamic> classes;

  const SessionsEnCoursPage({
    Key? key,
    required this.classes,
  }) : super(key: key);

  @override
  State<SessionsEnCoursPage> createState() => _SessionsEnCoursPageState();
}

class _SessionsEnCoursPageState extends State<SessionsEnCoursPage> {
  final Requete requete = Requete();
  final Map<String, Future<List<Map<String, dynamic>>>> _sessionFutures = {};

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

  Future<List<Map<String, dynamic>>> _loadSessions(String classId) async {
    final response = await requete.listSessionsByClass(classId);
    if (response.statusCode == 200 || response.statusCode == 201) {
      final body = response.body;
      if (body is List) {
        final sessions = body.map((e) => Map<String, dynamic>.from(e)).toList();
        return sessions
            .where((s) => (s['status'] ?? '').toString().toUpperCase() == 'LIVE')
            .toList();
      }
    }
    return [];
  }

  Future<List<Map<String, dynamic>>> _getSessions(String classId) {
    return _sessionFutures.putIfAbsent(classId, () => _loadSessions(classId));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Sessions en cours')),
      body: ListView.builder(
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
              leading: const Icon(Icons.live_tv),
              children: [
                FutureBuilder<List<Map<String, dynamic>>>(
                  future: _getSessions(classId),
                  builder: (context, snapshot) {
                    if (snapshot.connectionState == ConnectionState.waiting) {
                      return const Padding(
                        padding: EdgeInsets.all(16),
                        child: CircularProgressIndicator(),
                      );
                    }
                    final sessions = snapshot.data ?? [];
                    if (sessions.isEmpty) {
                      return const Padding(
                        padding: EdgeInsets.all(16),
                        child: Text('Aucune session en cours'),
                      );
                    }
                    return Column(
                      children: sessions.map((item) {
                        return ListTile(
                          title: Text(item['title'] ?? 'Session'),
                          subtitle: Text(
                            'Démarré: ${_formatDate(item['startedAt'] ?? item['scheduledAt'])}',
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
    );
  }
}
