import 'package:epst_windows_app/pages/formation_distante/Classe.dart';
import 'package:epst_windows_app/pages/formation_distante/ClasseService.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'inspecteur_cours_provider.dart';
import 'inspecteur_cours_service.dart';

class HorairesAdminScreen extends StatefulWidget {
  final Map user;

  const HorairesAdminScreen(this.user, {Key? key}) : super(key: key);

  @override
  State<HorairesAdminScreen> createState() => _HorairesAdminScreenState();
}

class _HorairesAdminScreenState extends State<HorairesAdminScreen> {
  final TextEditingController _titleController = TextEditingController();
  final DateFormat _formatter = DateFormat('dd/MM/yyyy HH:mm');

  List<Classe> _classes = [];
  Classe? _selectedClasse;
  String _audience = 'STUDENT';
  DateTime? _startsAt;
  DateTime? _endsAt;

  List<Map<String, dynamic>> _schedules = [];
  bool _loadingClasses = false;
  bool _loadingSchedules = false;

  @override
  void initState() {
    super.initState();
    _loadClasses();
  }

  Future<void> _loadClasses() async {
    setState(() => _loadingClasses = true);
    try {
      final classes = await ClasseService.getAllClasses();
      setState(() => _classes = classes);
    } finally {
      setState(() => _loadingClasses = false);
    }
  }

  Future<void> _loadSchedules() async {
    if (_selectedClasse == null) return;
    setState(() => _loadingSchedules = true);
    try {
      final items = await OnlineScheduleService.getByClass(
        _selectedClasse!.id,
        audience: _audience,
      );
      setState(() => _schedules = items);
    } catch (_) {
      setState(() => _schedules = []);
    } finally {
      setState(() => _loadingSchedules = false);
    }
  }

  Future<void> _pickDateTime({required bool isStart}) async {
    final date = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(2023),
      lastDate: DateTime(2100),
    );
    if (date == null) return;

    final time = await showTimePicker(
      context: context,
      initialTime: TimeOfDay.now(),
    );
    if (time == null) return;

    final dt = DateTime(
      date.year,
      date.month,
      date.day,
      time.hour,
      time.minute,
    );

    setState(() {
      if (isStart) {
        _startsAt = dt;
      } else {
        _endsAt = dt;
      }
    });
  }

  Future<void> _createSchedule() async {
    if (_selectedClasse == null || _startsAt == null || _endsAt == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
            content: Text('Veuillez s�lectionner la classe et les dates')),
      );
      return;
    }

    try {
      await OnlineScheduleService.create(
        classId: _selectedClasse!.id,
        title: _titleController.text.isEmpty ? 'Cours' : _titleController.text,
        audience: _audience,
        startsAt: _startsAt!.toIso8601String(),
        endsAt: _endsAt!.toIso8601String(),
        createdByMatricule: widget.user['matricule']?.toString(),
      );
      _titleController.clear();
      await _loadSchedules();
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Horaire cr�� avec succ�s')),
      );
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Erreur: $e')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Horaires des cours')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            Row(
              children: [
                Expanded(
                  child: _loadingClasses
                      ? const LinearProgressIndicator()
                      : DropdownButtonFormField<Classe>(
                          value: _selectedClasse,
                          decoration:
                              const InputDecoration(labelText: 'Classe'),
                          items: _classes
                              .map(
                                (c) => DropdownMenuItem(
                                  value: c,
                                  child:
                                      Text(c.label.isNotEmpty ? c.label : c.id),
                                ),
                              )
                              .toList(),
                          onChanged: (value) {
                            setState(() => _selectedClasse = value);
                            _loadSchedules();
                          },
                        ),
                ),
                const SizedBox(width: 12),
                SizedBox(
                  width: 180,
                  child: DropdownButtonFormField<String>(
                    value: _audience,
                    decoration: const InputDecoration(labelText: 'Audience'),
                    items: const [
                      DropdownMenuItem(value: 'STUDENT', child: Text('�l�ves')),
                      DropdownMenuItem(
                          value: 'TEACHER', child: Text('Enseignants')),
                    ],
                    onChanged: (value) {
                      if (value == null) return;
                      setState(() => _audience = value);
                      _loadSchedules();
                    },
                  ),
                ),
              ],
            ),
            const SizedBox(height: 12),
            TextField(
              controller: _titleController,
              decoration: const InputDecoration(labelText: 'Titre du cours'),
            ),
            const SizedBox(height: 12),
            Row(
              children: [
                Expanded(
                  child: OutlinedButton(
                    onPressed: () => _pickDateTime(isStart: true),
                    child: Text(
                      _startsAt == null
                          ? 'D�but'
                          : _formatter.format(_startsAt!),
                    ),
                  ),
                ),
                const SizedBox(width: 12),
                Expanded(
                  child: OutlinedButton(
                    onPressed: () => _pickDateTime(isStart: false),
                    child: Text(
                      _endsAt == null ? 'Fin' : _formatter.format(_endsAt!),
                    ),
                  ),
                ),
                const SizedBox(width: 12),
                ElevatedButton(
                  onPressed: _createSchedule,
                  child: const Text('Cr�er'),
                ),
              ],
            ),
            const SizedBox(height: 16),
            Expanded(
              child: _loadingSchedules
                  ? const Center(child: CircularProgressIndicator())
                  : _schedules.isEmpty
                      ? const Center(child: Text('Aucun horaire enregistr�'))
                      : ListView.builder(
                          itemCount: _schedules.length,
                          itemBuilder: (context, index) {
                            final item = _schedules[index];
                            return Card(
                              child: ListTile(
                                title: Text(item['title'] ?? 'Cours'),
                                subtitle: Text(
                                  '${item['startsAt']} ? ${item['endsAt']}',
                                ),
                                trailing: Text(item['audience'] ?? ''),
                              ),
                            );
                          },
                        ),
            ),
          ],
        ),
      ),
    );
  }
}
