export class LayerGroup {

  name: string;
  orderLayerGroup: number;
  layerGroupUpper: LayerGroup;
  draft: LayerGroup;
  layersGroup: LayerGroup[];
  published: boolean;
  hasChildren: boolean;

  constructor() {


  }

}