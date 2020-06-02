package com.labor.spring.system.ppp;

import java.io.IOException;
import java.util.List;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.collection.AhoCorasick.AhoCorasickDoubleArrayTrie;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import com.hankcs.hanlp.dictionary.CoreDictionary;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.dictionary.py.Pinyin;
import com.hankcs.hanlp.model.crf.CRFLexicalAnalyzer;
import com.hankcs.hanlp.suggest.Suggester;
import com.hankcs.hanlp.tokenizer.SpeedTokenizer;

//https://github.com/hankcs/HanLP
public class HanLPExtractor {
	
	private static final int defaultTopN = 1;
	
	public static List<String> keyword(String text){
		return HanLP.extractKeyword(text, defaultTopN);
	}
	
	public static List<String> phrase(String text){
		return HanLP.extractPhrase(text, defaultTopN);
	}

	public static List<String> summary(String text){
		return HanLP.extractSummary(text, defaultTopN);
	}
	
	public static List<String> keyword(String text, int topn){
		return HanLP.extractKeyword(text, topn);
	}
	
	public static List<String> phrase(String text, int topn){
		return HanLP.extractPhrase(text, topn);
	}
	
	public static List<String> summary(String text, int topn){
		return HanLP.extractSummary(text, topn);
	}
	
	public static String pinyin(String text){
		StringBuffer ret = new StringBuffer();
		List<Pinyin> pinyinList = HanLP.convertToPinyinList(text);
		for (Pinyin pinyin : pinyinList) {
			String tmp = pinyin.getPinyinWithoutTone();
			if (tmp!=null&&!tmp.equals("none")){
				ret.append(pinyin.getPinyinWithoutTone());
			}
		}
		return ret.toString();
	}
	
	
//	public static String content = "“数字包容，就是要让数字技术促进更包容的发展，不落下任何一个人、一个家庭、一个组织”。HUAWEI CONNECT 2019期间，在以“数字技术创新，推进社会经济全面可持续发展”为主题的数字包容峰会上，华为公司副董事长胡厚崑阐释了华为“TECH4ALL数字包容”行动计划，分享了项目进展，并倡议更多个人、组织加入进来，携手解决全球健康、教育、发展及环境等问题，在未来5年，再让全球5亿人从数字技术中受益。华为公司副董事长胡厚崑在数字包容峰会上发言让科技更有温度 给世界更多爱和感动科技进步正在加速智能社会的到来，我们在享受科技带来的种种便利的同时，也看到数字鸿沟依然存在：中国的老人，站在街头可能打不到车，因为大多数城市都在通行手机预订；孟加拉的女性，没有太多机会学习使用电脑，因为她们无法随意出门；科摩罗的岛民，曾经几乎与世隔绝，因为他们的通信网络这两年才开通……作为智能社会基础设施的ICT产业，已在推动国家经济增长、社会福祉发展等方面发挥着重要作用，也将加速联合国可持续发展目标（SDG）的达成，从而应对全球包括贫困、不平等、气候、环境退化、繁荣以及健康在内的各项挑战，不让任何一个人在数字世界中掉队。致力于让科技普济大众，让科技更有温度，华为从未停止过努力的脚步。此次，华为正式启动“TECH4ALL数字包容”行动计划，“数字包容”是指个人和群体平等获取（Access）和使用（Use）信息及通讯技术(ICT)&nbsp;的机会；“TECH4ALL”则是华为面向“数字包容”话题的倡议和长期行动举措。非洲的孩子，能看到外面的世界了；聋哑孩童开始享受阅读带来的无穷乐趣；人们开始听懂鲸鱼们恋爱的频率……通常，技术被视为一种工具，它使我们与自然世界渐行渐远，而越来越多这些真实故事的发生，正在感动着高速发展的世界。“科技不应高居象牙塔，而要普济天下，我们坚信数字技术将惠及每个人、每个家庭、每个组织。” 胡厚崑说。技术、应用、技能：数字包容从这里入手华为的愿景与使命，就是把数字世界带入每个人、每个家庭、每个组织，构建万物互联的智能世界。”数字包容”则是华为愿景在社会价值层面的最好体现，在这一愿景之下，“TECH4ALL”应运而生。“数字技术正在重塑着世界，我们要让所有人从中受益，要确保全面的数字包容，技术是基础，应用是关键，技能是保障。” 胡厚崑介绍说，“TECH4ALL” 将围绕技术、应用和技能三个方面来开展。技术方面，华为将通过联接、人工智能、云、移动终端等技术创新，持续降低联接的成本和消除覆盖障碍。应用方面，华为将赋能生态系统，帮助开发人员为不同的社区和行业创建更多应用程序。技能方面，华为将与政府，当地社区和其他行业合作，提升全社会的数字技能。“我们必须有一个清晰的努力方向，才能更好地发挥特长实现价值”。胡厚崑介绍说，在确定“TECH4ALL”的努力方向时，华为根据ICT行业可影响的方向以及“数字包容”的内涵，并匹配华为能力可及的方向，最终将围绕四大领域全面推进“数字包容”：促进健康福祉，通过数字技术为更广泛的人群提供更便捷的获取医疗资源的方法。平等优质教育，为弱势群体，如妇女、失学儿童、青少年等普及和提供教育机会，丰富教育资源。推进均衡发展，消除不同行业、企业、地区以及群体发展的差异，实现数字资源的平等享用和技术普惠。保护脆弱环境，以创新技术帮助NGO组织更高效地保护和维系良好的生态环境。截止目前，华为已经保障了全球30多亿人的通信畅通，支持170多个国家和地区1500多张网络的稳定运行。胡厚崑表示，我们希望在未来的五年内，再让5亿人从数字技术中受益。这个受益不限于联接，还包括通过AI/云/智能终端等技术、应用以及技能所能实现的受益。今天只是迈出了一小步，期待有更多的人加入儿科被称为“哑科”，婴幼儿先天性眼疾更是“哑科中的难题”。很多时候，小孩子眼睛不舒服只能用哭闹表达，常常让父母觉得孩子是在无理取闹。由于父母缺乏相应的知识和常识，大多数可能患眼疾的孩子长期处于未诊断状态，以致错过最佳的诊断期，影响一生。在西班牙，华为联合当地医疗研究机构IISAragon及DIVE研究中心，开发了一种新的检测幼儿视觉的医疗工具，小孩子只需要观看华为Matebook E屏幕显示的眼球刺激信号，DIVE系统实时收集孩子眼球移动轨迹和反应，并将数据发送到华为P30。借助华为P30的HiAI及机器学习能力，进行本地分析，筛查眼疾，这就是“Track AI”。在数字技术的支撑下，经过培训的父母也能像熟练的医生一样，更快、更简单、更有效的检测儿童视觉障碍，这将给世界上1900万个视觉功能障碍儿童带来福音。这只是“TECH4ALL”行动计划的一个缩影。目前华为TECH4ALL已经联合许多国际NGO组织，在更广阔的范围内着力解决健康、教育、发展和环境领域出现的问题。在肯尼亚，华为携手联合国教科文组织（UN UNESCO），比利时非营利组织Close the Gap等，将集装箱卡车变身为移动数字课堂，为非洲偏远地区的乡村教师、农村女性与创业青年提供数字技能培训，平等获得数字教育资源，无数年轻人的命运就此改变。在热带雨林，华为联合Rainforest Connection组织，将大量的华为旧手机改造为太阳能雨林监听设备，变成雨林生态的“耳朵”，通过AI时刻倾听且识别雨林中的盗伐卡车和电锯的声音，成为保护雨林的一线“声”机，帮助国际环保组织更高效地保护地球。据了解，明年涉及更多国家的6000平方公里热带雨林有望受益。……数字包容峰会现场，吸引了来自UNESCO、WWF、WEF等国际组织的代表，来自RFCx、DIVEMedical等NGO组织的代表，以及来自政府和企业的代表、行业意见领袖，他们一起为“TECH4ALL”行动计划献计献策。联合国教科文组织（UN UNESCO）东非负责人Ms. Ann Therese&nbsp;提出，ICT和AI技术将加速实现联合国可持续发展目标（SDG），并宣布加盟华为“TECH4ALL”项目——移动数字课堂DigiTruck，为偏远地区的乡村教师、农村妇女等提供数字赋能。世界自然基金会WWF提出“科技守护生物多样性”的计划，把人工智能与濒危动物监测、研究和保护有机融合，进一步维护生物多样性，更好的保护自然生态环境。而来自雨林联接组织RFCx的代表，以及DIVE Medical的代表，表示将把保护雨林和通过AI进行儿童视力筛查的实践经验进行复制和推广，让更多的区域和个人从数字技术中获得帮助。“‘TECH4ALL’希望让更多的人、家庭和组织享受数字世界的美好，我们要让特殊人不特殊，让普通人不普通，这就是技术的独特价值”，胡厚崑说。在“TECH4ALL”数字包容峰会现场，他向大家发出了“联合行动”的邀请：数字包容的推进，需要企业、政府、社会的共同努力。目前我们只是迈出了一小步，希望更多的个人、组织加入进来。&nbsp;";
	public static String content = "It is our desire that the [Project Name] CCB be chartered (Attach.1) with the authority to act as the management organization responsible for ensuring the documentation and control of [Project Name] software development by ensuring proper establishment, documentation and tracking of system requirements.  In addition, the CCB will serve as the forum to coordinate software changes between represented agencies and assess the impacts caused by requirements changes";
	public static void main(String[] args) {
		
		
//		System.out.println(HanLPExtractor.pinyin("哈哈this牛 is nota"));
//		List<String> keywordList = HanLP.extractKeyword(content, 5);
//		System.out.println(keywordList);
//		List<String> sentenceList = HanLP.extractSummary(content, 3);
//		System.out.println(sentenceList);
//		List<String> phraseList = HanLP.extractPhrase(content, 10);
//		System.out.println(phraseList);
//		
//		System.out.println(HanLP.segment("你好，欢迎使用HanLP汉语处理包！"));
		try {
			crfLexicalAnalyzer();
//			demoDependencyParser();
//			demoHighSpeedSegment();
//			convertToPinyinList();
//			suggest();
//			custom();
			

		} catch (Exception e) {
			
		}
	}

	public static void crfLexicalAnalyzer() throws IOException {
		CRFLexicalAnalyzer analyzer = new CRFLexicalAnalyzer();
        String[] tests = new String[]{
            "商品和服务",
            "上海华安工业（集团）公司董事长谭旭光和秘书胡花蕊来到美国纽约现代艺术博物馆参观",
            "微软公司於1975年由比爾·蓋茲和保羅·艾倫創立，18年啟動以智慧雲端、前端為導向的大改組。" // 支持繁体中文
        };
        for (String sentence : tests)
        {
            System.out.println(analyzer.analyze(sentence));
        }
	}

	public static void demoDependencyParser() throws IOException {
		CoNLLSentence sentence = HanLP.parseDependency("徐先生还具体帮助他确定了把画雄鹰、松鼠和麻雀作为主攻目标。");
        System.out.println(sentence);
        // 可以方便地遍历它
        for (CoNLLWord word : sentence)
        {
            System.out.printf("%s --(%s)--> %s\n", word.LEMMA, word.DEPREL, word.HEAD.LEMMA);
        }
        // 也可以直接拿到数组，任意顺序或逆序遍历
        CoNLLWord[] wordArray = sentence.getWordArray();
        for (int i = wordArray.length - 1; i >= 0; i--)
        {
            CoNLLWord word = wordArray[i];
            System.out.printf("%s --(%s)--> %s\n", word.LEMMA, word.DEPREL, word.HEAD.LEMMA);
        }
        // 还可以直接遍历子树，从某棵子树的某个节点一路遍历到虚根
        CoNLLWord head = wordArray[12];
        while ((head = head.HEAD) != null)
        {
            if (head == CoNLLWord.ROOT) System.out.println(head.LEMMA);
            else System.out.printf("%s --(%s)--> ", head.LEMMA, head.DEPREL);
        }
	}

	public static void demoHighSpeedSegment( ) {
		String text = "江西鄱阳湖干枯，中国最大淡水湖变成大草原";
		System.out.println(SpeedTokenizer.segment(text));
		long start = System.currentTimeMillis();
		int pressure = 1000000;
		for (int i = 0; i < pressure; ++i) {
			SpeedTokenizer.segment(text);
		}
		double costTime = (System.currentTimeMillis() - start) / (double) 1000;
		System.out.printf("分词速度：%.2f字每秒", text.length() * pressure / costTime);
	}

	public static List<Pinyin> convertToPinyinList() {
		String text = "重载不是重任";
		List<Pinyin> pinyinList = HanLP.convertToPinyinList(text);
		System.out.print("原文,");
		for (char c : text.toCharArray()) {
			System.out.printf("%c,", c);
		}
		System.out.println();
		System.out.print("拼音（无音调）,");
		for (Pinyin pinyin : pinyinList) {
			System.out.printf("%s,", pinyin.getPinyinWithoutTone());
		}
		System.out.println();
		return pinyinList;
	}

	public static List<String> suggest() {
		Suggester suggester = new Suggester();
		String[] titleArray = ("威廉王子发表演说 呼吁保护野生动物\n" + "《时代》年度人物最终入围名单出炉 普京马云入选\n" + "“黑格比”横扫菲：菲吸取“海燕”经验及早疏散\n"
				+ "日本保密法将正式生效 日媒指其损害国民知情权\n" + "英报告说空气污染带来“公共健康危机”").split("\\n");
		for (String title : titleArray) {
			suggester.addSentence(title);
		}

		System.out.println(suggester.suggest("发言", 1)); // 语义
		System.out.println(suggester.suggest("危机公共", 1)); // 字符
		System.out.println(suggester.suggest("mayun", 1)); // 拼音
		return suggester.suggest("发言", 1);

	}
	
	public static void custom() {
		 // 动态增加
        CustomDictionary.add("攻城狮");
        // 强行插入
        CustomDictionary.insert("白富美", "nz 1024");
        // 删除词语（注释掉试试）
//        CustomDictionary.remove("攻城狮");
        System.out.println(CustomDictionary.add("单身狗", "nz 1024 n 1"));
        System.out.println(CustomDictionary.get("单身狗"));

        String text = "攻城狮逆袭单身狗，迎娶白富美，走上人生巅峰";  // 怎么可能噗哈哈！

        // AhoCorasickDoubleArrayTrie自动机扫描文本中出现的自定义词语
        final char[] charArray = text.toCharArray();
        CustomDictionary.parseText(charArray, new AhoCorasickDoubleArrayTrie.IHit<CoreDictionary.Attribute>()
        {
            @Override
            public void hit(int begin, int end, CoreDictionary.Attribute value)
            {
                System.out.printf("[%d:%d]=%s %s\n", begin, end, new String(charArray, begin, end - begin), value);
            }
        });

        // 自定义词典在所有分词器中都有效
        System.out.println(HanLP.segment(text));
	}

}
