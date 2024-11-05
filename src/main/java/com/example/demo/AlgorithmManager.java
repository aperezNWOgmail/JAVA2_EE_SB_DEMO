package com.example.demo;

public class AlgorithmManager {

    public static String GenerateRandomPoints(int p_vertexSize, int p_sampleSize, int p_sourcePoint) {
        /*
         * //
         * int[,] graph = new int[vertexSize, vertexSize];
         * //
         * string statusMessage = string.Empty;
         * DateTime dt = DateTime.Now;
         * //
         * Random rand_x = new Random(dt.Millisecond / 2);
         * Random rand_y = new Random(dt.Millisecond * 2);
         * //
         * int[] vertex_X = FisherYates(sampleSize, rand_x);
         * int[] vertex_Y = FisherYates(sampleSize, rand_y);
         * List<string> vertexArray = new List<string>();
         * //
         * for (int index = 0; index < vertexSize; index++)
         * {
         * //
         * string separator_1 = (index < vertexSize - 1) ? "|" : "";
         * //
         * vertexArray.Add(string.Format("[{0},{1}]{2}", vertex_X[index],
         * vertex_Y[index], separator_1));
         * }
         * //
         * StringBuilder vertexArrayString = new StringBuilder();
         * //
         * for (int index = 0; index < vertexSize; index++)
         * {
         * vertexArrayString.Append(vertexArray[index]);
         * }
         * //
         * string separator_2 = "■";
         * //
         * string vertexMatrix = GenerateRandomMatrix(vertexArray, graph,vertexSize);
         * //
         * string vertexList = Dijkstra(vertexArray, graph, vertexSize, sampleSize,
         * sourcePoint);
         * //
         * string sortedListEncoded = string.Empty;
         * sortedListEncoded = HttpUtility.HtmlEncode(vertexList);
         * sortedListEncoded = sortedListEncoded.Replace(@",", @"<br/>");
         * sortedListEncoded = sortedListEncoded.Replace(@"\t", @"&nbsp;");
         * //
         * statusMessage = string.Format("{0}{1}{2}{1}{3}", vertexArrayString,
         * separator_2, vertexMatrix, sortedListEncoded);
         * //
         * //LogModel.Log(string.Format("DIJSTRA_DEMO. GENERATE_RANDOM_VERTEX : {0}",
         * statusMessage));
         * //
         * return statusMessage;
         */
        //
        String text = "[2,20]|[15,22]|[1,17]|[8,19]|[14,6]|[13,8]|[5,12]|[4,14]|[22,5]&#x25A0;{0,0,0,6,18,16,0,0,0}|{0,0,14,0,0,0,0,0,18}|{0,14,0,0,0,0,6,0,24}|{6,0,0,0,0,12,0,6,0}|{18,0,0,0,0,0,0,0,0}|{16,0,0,12,0,0,8,0,0}|{0,0,6,0,0,8,0,2,18}|{0,0,0,6,0,0,2,0,0}|{0,18,24,0,0,0,18,0,0}&#x25A0;00&lt;[2;20]&gt;-00-<br/>01&lt;[15;22]&gt;-34-[0;3]&#x2261;[3;7]&#x2261;[7;6]&#x2261;[6;2]&#x2261;[2;1]&#x2261;<br/>02&lt;[1;17]&gt;-20-[0;3]&#x2261;[3;7]&#x2261;[7;6]&#x2261;[6;2]&#x2261;<br/>03&lt;[8;19]&gt;-06-[0;3]&#x2261;<br/>04&lt;[14;6]&gt;-18-[0;4]&#x2261;<br/>05&lt;[13;8]&gt;-16-[0;5]&#x2261;<br/>06&lt;[5;12]&gt;-14-[0;3]&#x2261;[3;7]&#x2261;[7;6]&#x2261;<br/>07&lt;[4;14]&gt;-12-[0;3]&#x2261;[3;7]&#x2261;<br/>08&lt;[22;5]&gt;-32-[0;3]&#x2261;[3;7]&#x2261;[7;6]&#x2261;[6;8]&#x2261;";

        return text;
    }

    /*
     * 
     * public static string GenerateRandomMatrix(List<string> vertexString, int[,]
     * graph, int vertexSize)
     * {
     * //--------------------------------------------------------------
     * // DECLARACION DE VARIABLES
     * //--------------------------------------------------------------
     * StringBuilder statusMessage = new StringBuilder();
     * //
     * //--------------------------------------------------------------
     * // LA PARTE DIAGONAL DE LA MATRIZ SIEMPRE SERA 0
     * //--------------------------------------------------------------
     * for (int index = 0; index < vertexSize; index++)
     * {
     * graph[index, index] = 0;
     * }
     * //--------------------------------------------------------------
     * // LLENAR EL RESTO DE LA MATRIZ DE FORMA ALEATORIA
     * //--------------------------------------------------------------
     * //
     * DateTime dt = DateTime.Now;
     * Random rnd = new Random(dt.Millisecond * 3);
     * //
     * for (int index_x = 0; index_x < vertexSize; index_x++)
     * {
     * 
     * //
     * for (int index_y = (index_x + 1); index_y < vertexSize; index_y++)
     * {
     * //
     * double randomValue = rnd.Next(0, 2);
     * 
     * //--------------------------------------------------------------
     * // EN VALORES POSITIVOS LLENAR LA MATRIZ CON DISTANCIAS
     * //--------------------------------------------------------------
     * 
     * if (randomValue == 1)
     * {
     * //
     * randomValue = GetHipotemuza(vertexString, index_x, index_y);
     * }
     * 
     * //
     * graph[index_x, index_y] = Convert.ToInt32(randomValue);
     * graph[index_y, index_x] = Convert.ToInt32(randomValue);
     * }
     * }
     * 
     * //----------------------------------------------------
     * // GARANTIZAR CONECTIVIDAD DE AL MENOS UN PUNTO
     * //----------------------------------------------------
     * for (int index_x = 0; index_x < vertexSize; index_x++)
     * {
     * //
     * int zeroCount = 0;
     * 
     * //
     * for (int index_y = 0; index_y < vertexSize; index_y++)
     * {
     * // DESCARTA DIAGONAL Y VERIFICAR EXISTENCIA DE VALOR "CERO"
     * if ((index_x != index_y) && (graph[index_x,index_y] == 0))
     * {
     * //
     * zeroCount++;
     * 
     * // GARANTIZAR CONECTIVIDAD DE AL MENOS UN PUNTO
     * if (zeroCount == (vertexSize - 1))
     * {
     * //
     * int hipotemuza = Convert.ToInt32(GetHipotemuza(vertexString, index_x,
     * index_y));
     * graph[index_x, index_y] = hipotemuza;
     * graph[index_y, index_x] = hipotemuza;
     * }
     * }
     * }
     * }
     * 
     * 
     * //--------------------------------------------------------------------
     * // REPRESENTAR MATRIZ EN CADENA
     * //--------------------------------------------------------------------
     * //
     * for (int index_x = 0; index_x < vertexSize; index_x++)
     * {
     * //
     * string separator_1 = (index_x < vertexSize - 1) ? "|" : "";
     * //
     * StringBuilder stringArray = new StringBuilder();
     * //
     * string stringArrayValues = string.Empty;
     * //
     * for (int index_y = 0; index_y < vertexSize; index_y++)
     * {
     * //
     * string separator_2 = (index_y < vertexSize - 1) ? "," : "";
     * //
     * stringArray.Append(string.Format("{0}{1}", graph[index_x,
     * index_y].ToString(), separator_2));
     * }
     * //
     * stringArrayValues = string.Format("{{{0}}}", stringArray.ToString());
     * //
     * //LogModel.Log(string.
     * Format("DIJSTRA_DEMO. GENERATE_RANDOM_MATRIX : {0}|{1} ", index_x,
     * stringArrayValues));
     * //
     * statusMessage.Append(string.Format(@"{0}{1}", stringArrayValues,
     * separator_1));
     * }
     * return statusMessage.ToString();
     * }
     */

}
