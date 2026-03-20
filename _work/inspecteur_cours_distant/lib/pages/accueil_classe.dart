import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:get/get.dart';
import 'package:inspecteur_cours_distant/pages/cours/cours.dart';

class AccueilClasse extends StatelessWidget {
  //
  List options = ["Elève", "Direct", "Cours"];
  List svgs = [
    "HealthiconsBodyOutline",
    "HugeiconsAttachment02",
    "HugeiconsBookshelf03",
  ];
  //
  @override
  Widget build(BuildContext context) {
    //
    return Scaffold(
      appBar: AppBar(),
      body: GridView.count(
        padding: EdgeInsets.all(15),
        crossAxisCount: 2,
        children: List.generate(options.length, (o) {
          //
          return Card(
            elevation: 1,
            child: InkWell(
              onTap: () {
                //
                if (o == 2) {
                  //
                  Get.to(Cours());
                }
              },
              child: Column(
                children: [
                  Expanded(
                    flex: 6,
                    child: SvgPicture.asset(
                      "assets/${svgs[o]}.svg",
                      semanticsLabel: '${options[o]}',
                    ),
                  ),
                  Expanded(
                    flex: 3,
                    child: Center(
                      child: Column(
                        children: [
                          Text("${options[o]}", style: TextStyle(fontSize: 15)),
                          Text("${options[o]}", style: TextStyle(fontSize: 10)),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
            ),
          );
        }),
      ),
    );
  }

  //
}
