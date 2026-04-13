-- ============================
-- ALTER COLUMNS
-- ============================

ALTER TABLE communitymanagement.moderator
ALTER COLUMN assigned_by DROP NOT NULL;

ALTER TABLE communitymanagement.comment
ALTER COLUMN parent_comment_id DROP NOT NULL;

-- ============================
-- FUNCTION FOR TIMESTAMP AUDIT
-- ============================
--
--CREATE OR REPLACE FUNCTION communitymanagement.set_timestamps()
--RETURNS TRIGGER AS '
--BEGIN
--  IF TG_OP = ''INSERT'' THEN
--    NEW.created_at := CURRENT_TIMESTAMP;
--    NEW.updated_at := CURRENT_TIMESTAMP;
--  ELSIF TG_OP = ''UPDATE'' THEN
--    NEW.updated_at := CURRENT_TIMESTAMP;
--  END IF;
--  RETURN NEW;
--END;
--' LANGUAGE plpgsql;
--
---- ============================
---- TRIGGERS
---- ============================
--
--CREATE TRIGGER trg_community_timestamps
--BEFORE INSERT OR UPDATE ON communitymanagement.community
--FOR EACH ROW EXECUTE FUNCTION communitymanagement.set_timestamps();
--
--CREATE TRIGGER trg_community_member_timestamps
--BEFORE INSERT OR UPDATE ON communitymanagement.community_member
--FOR EACH ROW EXECUTE FUNCTION communitymanagement.set_timestamps();
--
--CREATE TRIGGER trg_moderator_timestamps
--BEFORE INSERT OR UPDATE ON communitymanagement.moderator
--FOR EACH ROW EXECUTE FUNCTION communitymanagement.set_timestamps();
--
--CREATE TRIGGER trg_privilege_timestamps
--BEFORE INSERT OR UPDATE ON communitymanagement.privilege
--FOR EACH ROW EXECUTE FUNCTION communitymanagement.set_timestamps();
--
--CREATE TRIGGER trg_moderator_privilege_timestamps
--BEFORE INSERT OR UPDATE ON communitymanagement.moderator_privilege
--FOR EACH ROW EXECUTE FUNCTION communitymanagement.set_timestamps();
--
--CREATE TRIGGER trg_post_timestamps
--BEFORE INSERT OR UPDATE ON communitymanagement.post
--FOR EACH ROW EXECUTE FUNCTION communitymanagement.set_timestamps();
--
--

