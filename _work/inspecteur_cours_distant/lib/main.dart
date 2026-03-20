import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:inspecteur_cours_distant/pages/accueil.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return GetMaterialApp(
      title: 'Inspecteur',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
      ),
      home: Accueil(51, "Joel Lungu"),
    );
  }
}
