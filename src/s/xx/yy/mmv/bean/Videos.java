package s.xx.yy.mmv.bean;

import java.io.Serializable;

public class Videos implements Serializable{
	private String videoId;
	private String type;
	private String videoUrl;
	private String name;
	private String flag;
	private String title;
	private String filmId;
	private String videoType;

	public String getVideoType() {
		return videoType;
	}

	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}

	public String getFilmId() {
		return filmId;
	}

	public void setFilmId(String filmId) {
		this.filmId = filmId;
	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
