export class LayerGroup {
  id: number;
  name: string;
  orderLayerGroup: number;
  layerGroupUpper: LayerGroup;
  draft: LayerGroup;
  layersGroup: LayerGroup[];
  nodes: LayerGroup[];
  groups: LayerGroup[];
  published: boolean;
  hasChildren: boolean;

  constructor() {


  }

}