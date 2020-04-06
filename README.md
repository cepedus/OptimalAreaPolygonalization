# Optimal Area Polygonalization
*Course project for INF562 - Computational Geometry from Theory to Applications (2019-2020), École Polytechnique*

Martín Cepeda & Cédric Javault

## Overview

Given a point cloud S of `n` 2D vertices, we implement two greedy approaches in order to approximate the MINimum Area simple Polygonalization and MAXimum Area simple Polygonalization of S (MINAP and MAXAP respectively), an NP-hard problem ([Fekete, 2000](https://doi.org/10.1007/pl00009492)). These two iterative approaches are based in the insertion of minimum area triangles at each step.

This problem was given at the [CG:SHOP 2019 Competition](https://cgshop.ibr.cs.tu-bs.de/competition/cg-shop-2019/) a year earlier.

## Requeriments

- Java 1.8
- core.jar ([Processing](https://processing.org/) 1.5.1 library, for 2D rendering of polygons and point clouds)
- TC.jar (for dealing with I/O operations on text files, developed for [INF361](https://www.enseignement.polytechnique.fr/informatique/INF361/classeTC/)) [doc](https://www.enseignement.polytechnique.fr/informatique/INF361/classeTC/tc/TC.html)
- Jcg.jar (Java library for computational geometry, developed for INF562 by [Luca Castelli Aleardi](http://www.lix.polytechnique.fr/~amturing/)) [doc](https://www.enseignement.polytechnique.fr/informatique/INF562/Java/doc/)

## Usage

`AreaOptimizer input_file_path` saves minumum/maximum area polygon found

`AreaOptimizer input_file_path solution_file_path` shows saved solution and score S=areaPolygon/areaConvexHull
